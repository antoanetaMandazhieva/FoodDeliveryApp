package com.example.fooddelivery.service.review;

import com.example.fooddelivery.dto.review.ReviewDto;

import java.util.List;

public interface SupplierReviewService {

    void addReview(Long clientId, Long supplierId, int rating, String comment);

    List<ReviewDto> getReviewsForSupplier(Long supplierId);
}