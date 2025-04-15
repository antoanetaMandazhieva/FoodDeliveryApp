package com.example.fooddelivery.controller.review;

import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.service.review.SupplierReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews/suppliers")
public class SupplierReviewController {

    private final SupplierReviewService reviewService;

    public SupplierReviewController(SupplierReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{supplierId}/add")
    public ResponseEntity<SupplierReviewDto> addReview(@PathVariable Long supplierId,
                                                       @RequestParam Long clientId,
                                                       @RequestParam int rating,
                                                       @RequestParam(required = false) String comment,
                                                       UriComponentsBuilder uriBuilder) {
        SupplierReviewDto savedReview = reviewService.addReview(clientId, supplierId, rating, comment);

        URI location = uriBuilder
                .path("/api/reviews/suppliers/{id}")
                .buildAndExpand(savedReview.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedReview);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<List<SupplierReviewDto>> getReviews(@PathVariable Long supplierId) {
        return ResponseEntity.ok(reviewService.getReviewsForSupplier(supplierId));
    }
}