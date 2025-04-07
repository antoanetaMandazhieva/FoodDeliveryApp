package com.example.fooddelivery.service.order;

import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.entity.Order;
import com.example.fooddelivery.entity.Product;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.ProductRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    /**
     * Този мап пази позволените смени на статуси, според реда за извършване на една заявка!
     */
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.ACCEPTED, OrderStatus.CANCELLED),
            OrderStatus.ACCEPTED, Set.of(OrderStatus.IN_DELIVERY),
            OrderStatus.IN_DELIVERY, Set.of(OrderStatus.DELIVERED),
            OrderStatus.DELIVERED, Set.of(),
            OrderStatus.CANCELLED, Set.of()
    );

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            RestaurantRepository restaurantRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
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

            order.addProduct(product);
        }

        order.calculateTotalPrice();
        order.setOrderStatus(OrderStatus.PENDING);

        return OrderMapper.mapFromOrderToDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void assignOrderToSupplier(Long orderId, Long supplierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getOrderStatus() != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Order already taken");
        }

        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        if (!"SUPPLIER".equals(supplier.getRole().getName())) {
            throw new IllegalArgumentException("Only users with SUPPLIER role can take orders");
        }

        if (order.getSupplier() != null) {
            throw new IllegalStateException("Order already has a supplier");
        }

        order.setSupplier(supplier);
        order.setOrderStatus(OrderStatus.IN_DELIVERY);
        orderRepository.save(order);

    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalArgumentException("Only employees can update order statuses");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getOrderStatus();

        if (!ALLOWED_TRANSITIONS.get(currentStatus).contains(newStatus)) {
            throw new IllegalStateException(String.format("Cannot change order status from %s to %s",
                    currentStatus, newStatus));
        }

        order.setOrderStatus(newStatus);

        orderRepository.save(order);
    }

    @Override
    public void cancelOrderByClient(Long orderId, Long clientId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!(order.getClient().getId() == clientId)) {
            throw new IllegalArgumentException("This order does not belong to the client");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only orders in PENDING status can be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDto> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(OrderMapper::mapFromOrderToDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId).stream()
                .map(OrderMapper::mapFromOrderToDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status).stream()
                .map(OrderMapper::mapFromOrderToDto)
                .toList();
    }

    @Override
    public BigDecimal getTotalRevenueBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByCreatedAtBetween(start, end).stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<OrderDto> getAvailableOrdersForSuppliers() {
        return orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.ACCEPTED)
                .stream()
                .map(OrderMapper::mapFromOrderToDto)
                .toList();
    }
}