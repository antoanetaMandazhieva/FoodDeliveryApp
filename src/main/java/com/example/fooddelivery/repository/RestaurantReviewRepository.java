package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {

}