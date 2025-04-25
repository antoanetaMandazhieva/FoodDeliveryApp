package com.example.fooddelivery.service.order;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.dto.order.*;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.order.Order;
import com.example.fooddelivery.entity.product.Product;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.Category;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.exception.order.*;
import com.example.fooddelivery.exception.role.InvalidRoleException;
import com.example.fooddelivery.repository.*;
import com.example.fooddelivery.service.discount.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.fooddelivery.util.SystemErrors.Order.*;
import static com.example.fooddelivery.util.SystemErrors.Product.*;
import static com.example.fooddelivery.util.SystemErrors.Restaurant.RESTAURANT_NOT_FOUND;
import static com.example.fooddelivery.util.SystemErrors.User.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String SUPPLIER_ROLE = "SUPPLIER";
    private static final String EMPLOYEE_ROLE = "EMPLOYEE";

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final DiscountService discountService;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            RestaurantRepository restaurantRepository,
                            AddressRepository addressRepository,
                            DiscountService discountService,
                            OrderMapper orderMapper,
                            AddressMapper addressMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.discountService = discountService;
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderCreateDto orderCreateDto, Long clientId) {
        User user = getUser(clientId);

        Address orderAddress = resolveClientAddress(orderCreateDto, user);

        Restaurant restaurant = getRestaurant(orderCreateDto.getRestaurantId());

        Order order = createInitialOrder(user, restaurant, orderAddress);


        // Важно: Нарочно се запазва тук за да имаме валидно id за поръчката което се използва в OrderedItem!
        orderRepository.save(order);

        // Мап, който държи Id на продуктите и тяхното количество
        Map<Long, Integer> productQuantityMap = getProductsAndTheirQuantity(orderCreateDto);

        // Този метод добавя продуктите към поръчката, ако всичко е успено. Ако не хвърля Exception
        addProductsToOrder(order, restaurant, user, productQuantityMap);

        // Връща отстъпката за конкретния потребител (може и null, ако няма такава)
        Discount discount = getDiscount(user);


        BigDecimal discountAmount = (discount != null) ? discount.getDiscountAmount() : BigDecimal.ZERO;

        if (discount != null) {
            // Свързва се отстъпката с поръчката
            discount.addOrder(order);
        }

        // Изчисляване на крайната цена на поръчката
        order.calculateTotalPrice(discountAmount);

        // Задава се начален статус на поръчката
        order.setOrderStatus(OrderStatus.PENDING);

        return orderMapper.mapFromOrderToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDto assignOrderToSupplier(Long orderId, Long supplierId) {
        try {
            User supplier = getUser(supplierId);
            validateIsSupplier(supplier, ONLY_SUPPLIER_ASSIGN_ORDERS);

            Order order = getOrder(orderId);

            validateOrderHasNoSupplier(order);

            OrderStatus status = order.getOrderStatus();
            if ((status != OrderStatus.ACCEPTED) && (status != OrderStatus.PREPARING)) {
                throw new InvalidOrderStatusException(WRONG_ORDER_STATUS_TO_TAKE_ORDER);
            }

            order.setSupplier(supplier);
            orderRepository.save(order);

            return orderMapper.mapFromOrderToDto(order);

        } catch (OptimisticLockException e) {
            throw new ConcurrentlyTakenOrderException(CONCURRENTLY_TAKEN_ORDER, e);
        }
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId, Long employeeId) {
        try {
            Order order = getOrder(orderId);

            User employee = getUser(employeeId);

            validateIsEmployee(employee, ONLY_EMPLOYEE_ACCEPT_ORDERS);

            if (order.getOrderStatus() != OrderStatus.PENDING) {
                throw new InvalidOrderStatusException(NOT_PENDING_STATUS);
            }

            order.setOrderStatus(OrderStatus.ACCEPTED);
            orderRepository.save(order);

        } catch (OptimisticLockException e) {
            throw new ConcurrentlyModifiedOrderException(CONCURRENTLY_MODIFIED_ORDER, e);
        }
    }

    @Override
    @Transactional
    public OrderStatusUpdateDto updateOrderStatus(Long orderId, Long employeeId) {
        try {
            User employee = getUser(employeeId);
            validateIsEmployee(employee, ONLY_EMPLOYEE_UPDATE_ORDERS);

            Order order = getOrder(orderId);

            OrderStatus currentStatus = order.getOrderStatus();

            if (currentStatus != OrderStatus.ACCEPTED) {
                throw new IllegalStateException(WRONG_ORDER_STATUS_TO_UPDATE_ORDER);
            }

            order.setOrderStatus(OrderStatus.PREPARING);

            orderRepository.save(order);

            OrderStatusUpdateDto dto = new OrderStatusUpdateDto();
            dto.setOrderId(orderId);
            dto.setNewStatus(String.valueOf(OrderStatus.PREPARING));

            return dto;

        } catch (OptimisticLockException e) {
            throw new ConcurrentlyModifiedOrderException(CONCURRENTLY_MODIFIED_ORDER, e);
        }
    }

    @Override
    public void takeOrder(Long orderId, Long supplierId) {
        Order order = getOrder(orderId);

        User supplier = getUser(supplierId);
        validateIsSupplier(supplier, ONLY_SUPPLIER_TAKE_ORDERS);

        if (order.getSupplier() == null || order.getSupplier().getId() != supplierId) {
            throw new InvalidOrderSupplierException(ORDER_NOT_FOR_SUPPLIER);
        }

        order.setOrderStatus(OrderStatus.IN_DELIVERY);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDto finishOrder(Long orderId, Long supplierId) {
        User supplier = getUser(supplierId);
        validateIsSupplier(supplier, ORDER_SUPPLIER_FINISH_ORDERS);

        Order order = getOrder(orderId);

        if (order.getOrderStatus() != OrderStatus.IN_DELIVERY) {
            throw new InvalidOrderStatusException(String.format(
                    WRONG_ORDER_STATUS_TO_FINISH_ORDER,
                    order.getOrderStatus()));
        }

        order.setOrderStatus(OrderStatus.DELIVERED);

        orderRepository.save(order);

        return orderMapper.mapFromOrderToDto(order);
    }

    @Override
    @Transactional
    public OrderDto cancelOrderByClient(Long orderId, Long clientId) {
        Order order = getOrder(orderId);

        validateIsOrderForClient(order, clientId);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(String.format(WRONG_ORDER_STATUS_TO_CANCEL_ORDER, order.getOrderStatus()));
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return orderMapper.mapFromOrderToDto(order);
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto getOrderInfoById(Long orderId, Long clientId) {
        Order order = getOrder(orderId);

        User user = getUser(clientId);

        validateIsOrderForClient(order, clientId);

        return orderMapper.toResponseDto(order);
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status, Long employeeId) {
        User employee = getUser(employeeId);
        validateIsEmployee(employee, ONLY_EMPLOYEE_SEE_ORDER_BY_STATUS);

        return orderRepository.findByOrderStatus(status).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalRevenueBetween(LocalDateTime start, LocalDateTime end, Long adminId) {
        User admin = getUser(adminId);
        validateIsAdmin(admin, ONLY_ADMIN_CHECK_TOTAL_REVENUE);

        return orderRepository.findByCreatedAtBetween(start, end).stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getAvailableOrdersForSuppliers(Long supplierId) {
        User user = getUser(supplierId);
        validateIsSupplier(user, ONLY_SUPPLIER_SEE_AVAILABLE_ORDERS);


        List<Order> availableOrders = orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.PREPARING);
        availableOrders.addAll(orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.ACCEPTED));

        return availableOrders
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private Address resolveClientAddress(OrderCreateDto dto, User user) {
        Address address = getOrderAddress(addressMapper.mapToAddress(dto.getAddress()), user);

        validateUserAddress(user, address);

        return address;
    }

    private Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                        .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND));
    }

    private Order createInitialOrder(User user, Restaurant restaurant, Address orderAddress) {
        Order order = new Order();
        order.setClient(user);
        order.setRestaurant(restaurant);
        order.setAddress(orderAddress);

        return order;
    }


    private Map<Long, Integer> getProductsAndTheirQuantity(OrderCreateDto orderCreateDto) {
        Map<Long, Integer> productQuantityMap = new HashMap<>();


        for (OrderProductDto dto : orderCreateDto.getProducts()) {
            Long productId = dto.getProductId();
            int quantity = dto.getQuantity();

            if (quantity <= 0) {
                throw new InvalidProductQuantityException(String.format(PRODUCT_QUANTITY_LESS_THAN_0, productId));
            }


            /* Коментар:
            1. Ако липсва продукта с това ID -> Добавя го със стойност 0 и след това му добавя даденото количество
            2. Ако го има този продукт с това ID -> Прибавя към старото количсетво и прави общ брой
             */
            productQuantityMap.putIfAbsent(productId, 0);
            productQuantityMap.put(productId, productQuantityMap.get(productId) + quantity);
        }
        return productQuantityMap;
    }

    private void addProductsToOrder(Order order, Restaurant restaurant, User client, Map<Long, Integer> productQuantityMap) {
        for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
            Long productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = getProduct(productId);

            if (!restaurant.getProducts().contains(product)) {
                throw new InvalidProductInRestaurantException(String.format(PRODUCT_NOT_IN_RESTAURANT,
                        product.getName(), restaurant.getName()));
            }

            if (product.getCategory() == Category.ALCOHOLS) {
                if (!client.isOver18()) {
                    throw new UnderageUserException(UNDERAGE_USER_CANNOT_ORDER_ALCOHOL);
                }
            }

            order.addOrderedItem(product, quantity);
        }
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
    }

    private Discount getDiscount(User client) {
        if ("CLIENT".equals(client.getRole().getName())) {
            return discountService.checkAndGiveClientDiscount(client);
        }

        return discountService.checkAndGiveWorkerDiscount(client);
    }

    private void validateIsSupplier(User user, String message) {
        if (!SUPPLIER_ROLE.equals(user.getRole().getName())) {
            throw new InvalidRoleException(message);
        }
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));
    }

    private void validateOrderHasNoSupplier(Order order) {
        if (order.getSupplier() != null) {
            throw new AlreadyTakenOrderException(ALREADY_TAKEN_ORDER);
        }
    }

    private void validateIsEmployee(User user, String message) {
        if (!EMPLOYEE_ROLE.equals(user.getRole().getName())) {
            throw new InvalidRoleException(message);
        }
    }

    private void validateIsAdmin(User user, String message) {
        if (!"ADMIN".equals(user.getRole().getName())) {
            throw new InvalidRoleException(message);
        }
    }

    private Address getOrderAddress(Address orderAddress, User user) {
        return addressRepository.findByStreetAndCityAndCountryAndUserId(orderAddress.getStreet(), orderAddress.getCity(),
                        orderAddress.getCountry(), user.getId())
                .orElseThrow(() -> new WrongUserAddressException(String.format(USER_ADDRESS_NOT_FOUND,
                        user.getName(), user.getSurname())));
    }

    private void validateUserAddress(User user, Address address) {
        if (!user.getAddresses().contains(address)) {
            throw new WrongUserAddressException(String.format(USER_ADDRESS_NOT_FOUND, user.getName(), user.getSurname()));
        }
    }

    private void validateIsOrderForClient(Order order, Long clientId) {
        if (!(order.getClient().getId() == clientId)) {
            throw new InvalidOrderClientException(ORDER_NOT_FOR_CLIENT);
        }
    }
}