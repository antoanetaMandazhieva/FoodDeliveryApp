package com.example.fooddelivery.service.review;

import com.example.fooddelivery.config.review.ReviewMapper;
import com.example.fooddelivery.dto.review.ReviewDto;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.entity.RestaurantReview;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.RestaurantReviewRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RestaurantReviewServiceImpl implements RestaurantReviewService {

    private final RestaurantReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper reviewMapper;

    public RestaurantReviewServiceImpl(RestaurantReviewRepository reviewRepository,
                                       UserRepository userRepository,
                                       RestaurantRepository restaurantRepository,
                                       ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    @Transactional
    public void addReview(Long clientId, Long restaurantId, int rating, String comment) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        RestaurantReview review = new RestaurantReview();
        review.setReviewer(client);
        review.setRestaurant(restaurant);
        review.setRating(rating);
        review.setComment(comment);

        updateRestaurantAverageRating(restaurantId);

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewDto> getReviewsForRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(reviewMapper::mapToDto)
                .toList();
    }

    private void updateRestaurantAverageRating(Long restaurantId) {
        List<RestaurantReview> reviews = reviewRepository.findByRestaurantId(restaurantId);

        BigDecimal avg = BigDecimal.valueOf(
                reviews.stream().mapToInt(RestaurantReview::getRating).average().orElse(0)
        ).setScale(1, RoundingMode.HALF_UP);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        restaurant.setAverageRating(avg);

        restaurantRepository.save(restaurant);
    }
}