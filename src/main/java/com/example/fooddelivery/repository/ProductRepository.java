package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByRestaurantIdAndIsAvailableTrue(Long restaurantId);
}