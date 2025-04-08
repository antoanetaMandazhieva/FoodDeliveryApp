package com.example.fooddelivery.service.order;

import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderCreateDto orderCreateDto, Long clientId);

    void assignOrderToSupplier(Long orderId, Long supplierId);

    void updateOrderStatus(Long orderId, OrderStatus newStatus, Long employeeId);

    OrderDto finishOrder(Long orderId, Long supplierId);

    void cancelOrderByClient(Long orderId, Long clientId);

    List<OrderResponseDto> getOrdersByClient(Long clientId);

    List<OrderResponseDto> getOrdersBySupplier(Long supplierId);

    List<OrderResponseDto> getOrdersByStatus(OrderStatus status);

    BigDecimal getTotalRevenueBetween(LocalDateTime start, LocalDateTime end);

    List<OrderResponseDto> getAvailableOrdersForSuppliers();
}