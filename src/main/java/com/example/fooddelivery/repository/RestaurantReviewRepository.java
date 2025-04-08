package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {

    List<RestaurantReview> findByRestaurantId(Long restaurantId);

    List<RestaurantReview> findByReviewerId(Long reviewerId);
}