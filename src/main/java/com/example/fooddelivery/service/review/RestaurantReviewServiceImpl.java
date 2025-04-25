package com.example.fooddelivery.service.review;

import com.example.fooddelivery.config.review.ReviewMapper;
import com.example.fooddelivery.dto.review.RestaurantReviewDto;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.entity.review.RestaurantReview;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.RestaurantReviewRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.example.fooddelivery.util.SystemErrors.Restaurant.RESTAURANT_NOT_FOUND;
import static com.example.fooddelivery.util.SystemErrors.User.USER_NOT_FOUND;

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
    public RestaurantReviewDto addReview(Long clientId, Long restaurantId, int rating, String comment) {
        User user = getUser(clientId);

        Restaurant restaurant = getRestaurant(restaurantId);

        RestaurantReview review = createReview(user, restaurant, rating, comment);
        reviewRepository.save(review);

        updateRestaurantAverageRating(restaurantId);

        return reviewMapper.mapToDto(review);
    }

    @Override
    @Transactional
    public List<RestaurantReviewDto> getReviewsForRestaurant(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(reviewMapper::mapToDto)
                .toList();
    }

    private User getUser(Long clientId) {
        return userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private Restaurant getRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND));
    }

    private RestaurantReview createReview(User user, Restaurant restaurant, int rating, String comment) {
        RestaurantReview review = new RestaurantReview();

        review.setReviewer(user);
        review.setRestaurant(restaurant);
        review.setRating(rating);
        review.setComment(comment);

        return review;
    }


    private void updateRestaurantAverageRating(Long restaurantId) {
        List<RestaurantReview> reviews = reviewRepository.findByRestaurantId(restaurantId);

        BigDecimal avg = calculateAverageRating(reviews);

        Restaurant restaurant = getRestaurant(restaurantId);

        restaurant.setAverageRating(avg);

        restaurantRepository.save(restaurant);
    }

    private BigDecimal calculateAverageRating(List<RestaurantReview> reviews) {
        return BigDecimal.valueOf(
                reviews.stream().mapToInt(RestaurantReview::getRating).average().orElse(0)
        ).setScale(1, RoundingMode.HALF_UP);
    }
}