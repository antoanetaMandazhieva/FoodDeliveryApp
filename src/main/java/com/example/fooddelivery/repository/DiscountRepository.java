package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByUserId(Long userId);

    long countByUserId(Long userId);
}