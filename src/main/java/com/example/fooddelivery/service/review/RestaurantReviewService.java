package com.example.fooddelivery.service.review;

import com.example.fooddelivery.dto.review.ReviewDto;

import java.util.List;

public interface RestaurantReviewService {

    void addReview(Long clientId, Long restaurantId, int rating, String comment);

    List<ReviewDto> getReviewsForRestaurant(Long restaurantId);
}