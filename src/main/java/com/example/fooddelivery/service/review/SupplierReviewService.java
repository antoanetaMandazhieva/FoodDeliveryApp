package com.example.fooddelivery.service.review;

import com.example.fooddelivery.dto.review.SupplierReviewDto;

import java.util.List;

public interface SupplierReviewService {

    SupplierReviewDto addReview(Long clientId, Long supplierId, int rating, String comment);

    List<SupplierReviewDto> getReviewsForSupplier(Long supplierId);
}