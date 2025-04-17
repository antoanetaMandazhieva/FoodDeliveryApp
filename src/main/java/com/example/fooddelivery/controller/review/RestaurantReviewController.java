package com.example.fooddelivery.controller.review;

import com.example.fooddelivery.dto.review.RestaurantReviewDto;
import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.service.review.RestaurantReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews/restaurants")
public class RestaurantReviewController {

    private final RestaurantReviewService reviewService;

    public RestaurantReviewController(RestaurantReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Tested!
    @PostMapping("/{restaurantId}/add")
    public ResponseEntity<RestaurantReviewDto> addReview(@PathVariable Long restaurantId,
                                                         @RequestParam Long clientId,
                                                         @RequestParam int rating,
                                                         @RequestParam(required = false) String comment,
                                                         UriComponentsBuilder uriBuilder) {
        RestaurantReviewDto savedReview = reviewService.addReview(clientId, restaurantId, rating, comment);

        URI location = uriBuilder
                .path("/api/reviews/restaurants/{id}")
                .buildAndExpand(savedReview.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedReview);
    }

    // Tested!
    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<RestaurantReviewDto>> getReviews(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getReviewsForRestaurant(restaurantId));
    }
}