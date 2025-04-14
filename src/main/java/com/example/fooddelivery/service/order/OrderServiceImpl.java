package com.example.fooddelivery.service.order;

import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.enums.Category;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.ProductRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.UserRepository;
import com.example.fooddelivery.service.bonus.BonusService;
import jakarta.persistence.EntityNotFoundException;
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
    private final BonusService bonusService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            RestaurantRepository restaurantRepository,
                            BonusService bonusService,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.bonusService = bonusService;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderCreateDto orderCreateDto, Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Restaurant restaurant = restaurantRepository.findById(orderCreateDto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        Order order = new Order();

        order.setClient(client);
        order.setRestaurant(restaurant);

        for (Long productId : orderCreateDto.getProductIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getCategory() == Category.ALCOHOLS) {
                if (!client.isOver18()) {
                    throw new IllegalArgumentException("You are underage and cannot order alcohol");
                }
            }

            order.addProduct(product);
        }

        order.calculateTotalPrice();
        order.setOrderStatus(OrderStatus.PENDING);

        return orderMapper.mapFromOrderToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void assignOrderToSupplier(Long orderId, Long supplierId) {
        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"SUPPLIER".equals(supplier.getRole().getName())) {
            throw new IllegalArgumentException("Only users with SUPPLIER role can take orders");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderStatus status = order.getOrderStatus();

        if ((status != OrderStatus.ACCEPTED) && (status != OrderStatus.PREPARING)) {
            throw new IllegalStateException("Order cannot be taken");
        }

        if (order.getSupplier() != null) {
            throw new IllegalStateException("Order already has a supplier");
        }

        order.setSupplier(supplier);
        orderRepository.save(order);

    }

    @Override
    public void acceptOrder(Long orderId, Long employeeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalStateException("Only employees can accept orders");
        }

        order.setOrderStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalStateException("Only employees can update order statuses");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getOrderStatus();

        OrderStatus newStatus = switch (currentStatus) {
            case PENDING -> OrderStatus.ACCEPTED;
            case ACCEPTED -> OrderStatus.PREPARING;
            default -> throw new IllegalStateException("You cannot change status currently.");
        };

        order.setOrderStatus(newStatus);

        orderRepository.save(order);
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

        if (order.getSupplier().getId() != supplierId) {
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
        bonusService.checkAndAddBonusForSupplier(supplier);


        orderRepository.save(order);

        return orderMapper.mapFromOrderToDto(order);
    }

    @Override
    public void cancelOrderByClient(Long orderId, Long clientId) {
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
    }


    @Override
    public List<OrderResponseDto> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalRevenueBetween(LocalDateTime start, LocalDateTime end, Long employeeId) {
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getRole().getName().equals("ADMIN")) {
            throw new RuntimeException("Access denied: Only admins can view revenue");
        }

        return orderRepository.findByCreatedAtBetween(start, end).stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<OrderResponseDto> getAvailableOrdersForSuppliers() {
        return orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.ACCEPTED)
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }
}