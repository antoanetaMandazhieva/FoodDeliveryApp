package com.example.fooddelivery.config.review;

import com.example.fooddelivery.dto.review.ReviewDto;
import com.example.fooddelivery.entity.RestaurantReview;
import com.example.fooddelivery.entity.SupplierReview;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper mapper;

    public ReviewMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ReviewDto mapToDto(SupplierReview review) {
        ReviewDto reviewDto = mapper.map(review, ReviewDto.class);

        if (reviewDto.getReviewerId() == null) {
            reviewDto.setReviewerId(review.getReviewer().getId());
        }

        return reviewDto;
    }

    public ReviewDto mapToDto(RestaurantReview review) {
        ReviewDto reviewDto = mapper.map(review, ReviewDto.class);

        if (reviewDto.getReviewerId() == null) {
            reviewDto.setReviewerId(review.getReviewer().getId());
        }

        return reviewDto;
    }
}
