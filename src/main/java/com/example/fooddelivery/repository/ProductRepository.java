package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByRestaurantIdAndIsAvailableTrue(Long restaurantId);

    Optional<Product> findByNameAndRestaurantName(String name, String restaurantName);

    List<Product> findAllByNameInAndRestaurantId(List<String> productNames, Long id);
}