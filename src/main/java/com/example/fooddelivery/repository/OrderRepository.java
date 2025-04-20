package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.order.Order;
import com.example.fooddelivery.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientId(Long clientId);

    List<Order> findBySupplierId(Long supplierId);

    List<Order> findByOrderStatus(OrderStatus status);

    List<Order> findByOrderStatusAndSupplierId(OrderStatus status, Long supplierId);

    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findByOrderStatusAndSupplierIsNull(OrderStatus status);
}