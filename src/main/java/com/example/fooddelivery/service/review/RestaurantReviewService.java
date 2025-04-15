package com.example.fooddelivery.service.review;

import com.example.fooddelivery.dto.review.RestaurantReviewDto;

import java.util.List;

public interface RestaurantReviewService {

    RestaurantReviewDto addReview(Long clientId, Long restaurantId, int rating, String comment);

    List<RestaurantReviewDto> getReviewsForRestaurant(Long restaurantId);
}