package com.example.fooddelivery.mapper.review;

import com.example.fooddelivery.dto.review.RestaurantReviewDto;
import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.entity.review.RestaurantReview;
import com.example.fooddelivery.entity.review.SupplierReview;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper mapper;

    public ReviewMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public SupplierReviewDto mapToDto(SupplierReview review) {
        SupplierReviewDto supplierReviewDto = mapper.map(review, SupplierReviewDto.class);

        if (supplierReviewDto.getReviewerId() == null) {
            supplierReviewDto.setReviewerId(review.getReviewer().getId());
        }

        if (supplierReviewDto.getId() == null) {
            supplierReviewDto.setId(review.getId());
        }

        return supplierReviewDto;
    }

    public RestaurantReviewDto mapToDto(RestaurantReview review) {
        RestaurantReviewDto restaurantReviewDto = mapper.map(review, RestaurantReviewDto.class);

        if (restaurantReviewDto.getReviewerId() == null) {
            restaurantReviewDto.setReviewerId(review.getReviewer().getId());
        }

        if (restaurantReviewDto.getId() == null) {
            restaurantReviewDto.setId(review.getId());
        }


        return restaurantReviewDto;
    }
}
