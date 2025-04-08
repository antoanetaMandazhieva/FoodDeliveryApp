package com.example.fooddelivery.config.review;

import com.example.fooddelivery.config.common.Mapper;
import com.example.fooddelivery.dto.review.ReviewDto;
import com.example.fooddelivery.entity.RestaurantReview;
import com.example.fooddelivery.entity.SupplierReview;
import org.modelmapper.ModelMapper;

public class ReviewMapper {

    private final static ModelMapper mapper = Mapper.getInstance();

    public static ReviewDto mapToDto(SupplierReview review) {
        ReviewDto reviewDto = mapper.map(review, ReviewDto.class);

        if (reviewDto.getReviewerId() == null) {
            reviewDto.setReviewerId(review.getReviewer().getId());
        }

        return reviewDto;
    }

    public static ReviewDto mapToDto(RestaurantReview review) {
        ReviewDto reviewDto = mapper.map(review, ReviewDto.class);

        if (reviewDto.getReviewerId() == null) {
            reviewDto.setReviewerId(review.getReviewer().getId());
        }

        return reviewDto;
    }
}
