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
import com.example.fooddelivery.repository.*;
import com.example.fooddelivery.service.discount.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

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
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Address orderAddress = getOrderAddress(addressMapper.mapToAddress(orderCreateDto.getAddress()), client);

        if (!client.getAddresses().contains(orderAddress)) {
            throw new IllegalArgumentException("Client doesn't have this address");
        }

        Restaurant restaurant = restaurantRepository.findById(orderCreateDto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        Order order = new Order();

        order.setClient(client);
        order.setRestaurant(restaurant);
        order.setAddress(orderAddress);


        // Важно: Нарочно се запазва тук за да имаме валидно id за поръчката което се използва в OrderedItem!
        orderRepository.save(order);

        // Мап, който държи Id на продуктите и тяхното количество
        Map<Long, Integer> productQuantityMap = getProductsAndTheirQuantity(orderCreateDto);

        // Този метод добавя продуктите към поръчката, ако всичко е успено. Ако не хвърля Exception
        addProductsToOrder(order, restaurant, client, productQuantityMap);

        // Връща отстъпката за конкретния потребител (може и null, ако няма такава)
        Discount discount = getDiscount(client);


        BigDecimal discountAmount;

        if (discount != null) {
            discount.addOrder(order);
            discountAmount = discount.getDiscountAmount();
        } else {
            discountAmount = BigDecimal.ZERO;
        }

        order.calculateTotalPrice(discountAmount);

        order.setOrderStatus(OrderStatus.PENDING);

        return orderMapper.mapFromOrderToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDto assignOrderToSupplier(Long orderId, Long supplierId) {
        try {
            User supplier = userRepository.findById(supplierId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (!"SUPPLIER".equals(supplier.getRole().getName())) {
                throw new IllegalArgumentException("Only users with SUPPLIER role can take orders");
            }

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));

            if (order.getSupplier() != null) {
                throw new IllegalStateException("Order already has a supplier");
            }

            OrderStatus status = order.getOrderStatus();
            if ((status != OrderStatus.ACCEPTED) && (status != OrderStatus.PREPARING)) {
                throw new IllegalStateException("Order cannot be taken");
            }

            order.setSupplier(supplier);
            orderRepository.save(order);

            return orderMapper.mapFromOrderToDto(order);

        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Order was taken by another supplier", e);
        }
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId, Long employeeId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));

            User user = getUser(employeeId);

            String message = "Only EMPLOYEES can accept order";
            validateIfIsEmployee(user, message);


            order.setOrderStatus(OrderStatus.ACCEPTED);
            orderRepository.save(order);

        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Order was modified by another employee. Please try again.", e);
        }
    }

    @Override
    @Transactional
    public OrderStatusUpdateDto updateOrderStatus(Long orderId, Long employeeId) {
        try {
            User employee = userRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

            if (!"EMPLOYEE".equals(employee.getRole().getName())) {
                throw new IllegalStateException("Only employees can update order statuses");
            }

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));

            OrderStatus currentStatus = order.getOrderStatus();

            if (currentStatus != OrderStatus.ACCEPTED) {
                throw new IllegalStateException("You cannot change status currently");
            }

            order.setOrderStatus(OrderStatus.PREPARING);

            orderRepository.save(order);

            OrderStatusUpdateDto dto = new OrderStatusUpdateDto();
            dto.setOrderId(orderId);
            dto.setNewStatus(String.valueOf(OrderStatus.PREPARING));

            return dto;

        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Order was modified concurrently. Try again.", e);
        }
    }

    @Override
    public void takeOrder(Long orderId, Long supplierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"SUPPLIER".equals(supplier.getRole().getName())) {
            throw new IllegalArgumentException("Only users with SUPPLIER role can take orders");
        }

        if (order.getSupplier() == null || order.getSupplier().getId() != supplierId) {
            throw new IllegalArgumentException("You can't take order which is not for you");
        }

        order.setOrderStatus(OrderStatus.IN_DELIVERY);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDto finishOrder(Long orderId, Long supplierId) {
        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"SUPPLIER".equals(supplier.getRole().getName())) {
            throw new IllegalStateException("Only suppliers can finish orders");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getOrderStatus() != OrderStatus.IN_DELIVERY) {
            throw new IllegalStateException(String.format(
                    "Order can't be finished unless it's in IN_DELIVERY status. Current status: %s",
                    order.getOrderStatus()));
        }

        order.setOrderStatus(OrderStatus.DELIVERED);

        orderRepository.save(order);

        return orderMapper.mapFromOrderToDto(order);
    }

    @Override
    @Transactional
    public OrderDto cancelOrderByClient(Long orderId, Long clientId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!(order.getClient().getId() == clientId)) {
            throw new IllegalArgumentException("This order does not belong to the client");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(String.format(
                    "Cannot cancel order with status: %s. Only PENDING orders can be cancelled.",
                    order.getOrderStatus()));
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
    public List<OrderResponseDto> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status, Long employeeId) {
        User employee = getUser(employeeId);

        String message = "Only EMPLOYEES can see all orders with given status";
        validateIfIsEmployee(employee, message);


        return orderRepository.findByOrderStatus(status).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalRevenueBetween(LocalDateTime start, LocalDateTime end, Long adminId) {
        User admin = getUser(adminId);

        String message = "Only ADMINS can see total revenue between dates";
        validateIfIsAdmin(admin, message);

        if (!admin.getRole().getName().equals("ADMIN")) {
            throw new RuntimeException("Access denied: Only admins can view revenue");
        }

        return orderRepository.findByCreatedAtBetween(start, end).stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getAvailableOrdersForSuppliers(Long supplierId) {
        User user = getUser(supplierId);

        String message = "You need SUPPLIER role to see available orders";
        validateIfIsSupplier(user, message);


        List<Order> availableOrders = orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.PREPARING);
        availableOrders.addAll(orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.ACCEPTED));

        return availableOrders
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    private Address getOrderAddress(Address orderAddress, User client) {
        return addressRepository.findByStreetAndCityAndCountryAndUserId(orderAddress.getStreet(), orderAddress.getCity(),
                                                               orderAddress.getCountry(), client.getId())
                            .orElseThrow(() -> new EntityNotFoundException(String.format("Client: %s %s doesn't have this address",
                                    client.getName(), client.getSurname())));
    }

    private static Map<Long, Integer> getProductsAndTheirQuantity(OrderCreateDto orderCreateDto) {
        Map<Long, Integer> productQuantityMap = new HashMap<>();


        for (OrderProductDto dto : orderCreateDto.getProducts()) {
            Long productId = dto.getProductId();
            int quantity = dto.getQuantity();

            if (quantity <= 0) {
                throw new IllegalArgumentException(String.format("Quantity for product with ID: %d must be greater than 0",
                        productId));
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

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (!restaurant.getProducts().contains(product)) {
                throw new IllegalArgumentException(String.format("Product: %s is not in Restaurant: %s",
                        product.getName(), restaurant.getName()));
            }

            if (product.getCategory() == Category.ALCOHOLS) {
                if (!client.isOver18()) {
                    throw new IllegalArgumentException("You are underage and cannot order alcohol");
                }
            }

            order.addOrderedItem(product, quantity);
        }
    }

    private Discount getDiscount(User client) {
        if ("CLIENT".equals(client.getRole().getName())) {
            return discountService.checkAndGiveClientDiscount(client);
        }

        return discountService.checkAndGiveWorkerDiscount(client);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private void validateIfIsSupplier(User user, String message) {
        if (!"SUPPLIER".equals(user.getRole().getName())) {
            throw new IllegalStateException(message);
        }
    }

    private void validateIfIsEmployee(User user, String message) {
        if (!"EMPLOYEE".equals(user.getRole().getName())) {
            throw new IllegalStateException(message);
        }
    }

    private void validateIfIsAdmin(User user, String message) {
        if (!"ADMIN".equals(user.getRole().getName())) {
            throw new IllegalStateException(message);
        }
    }
}