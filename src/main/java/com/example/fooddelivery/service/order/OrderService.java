package com.example.fooddelivery.service.order;

import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.order.OrderStatusUpdateDto;
import com.example.fooddelivery.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderCreateDto orderCreateDto, Long clientId);

    OrderDto assignOrderToSupplier(Long orderId, Long supplierId);

    void acceptOrder(Long orderId, Long employeeId);

    OrderStatusUpdateDto updateOrderStatus(Long orderId, Long employeeId);

    void takeOrder(Long orderId, Long supplierId);

    OrderDto finishOrder(Long orderId, Long supplierId);

    OrderDto cancelOrderByClient(Long orderId, Long clientId);

    List<OrderResponseDto> getOrdersByClient(Long clientId);

    OrderResponseDto getOrderInfoById(Long orderId, Long clientId);

    List<OrderResponseDto> getOrdersBySupplier(Long supplierId);

    List<OrderResponseDto> getOrdersByStatus(OrderStatus status, Long employeeId);

    BigDecimal getTotalRevenueBetween(LocalDateTime start, LocalDateTime end, Long employeeId);

    List<OrderResponseDto> getAvailableOrdersForSuppliers(Long supplierId);
}