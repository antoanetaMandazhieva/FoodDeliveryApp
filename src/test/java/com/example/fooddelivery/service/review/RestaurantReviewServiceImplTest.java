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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RestaurantReviewServiceImplTest {
    @Mock
    private RestaurantReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private RestaurantReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    private void setId(Object obj, long id) {
        try {
            Field field = obj.getClass().getSuperclass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(obj, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void addReview_shouldAddReviewSuccessfully() {
        User client = new User();
        Restaurant restaurant = new Restaurant();
        setId(client, 1L);
        setId(restaurant, 2L);

        RestaurantReview savedReview = new RestaurantReview();
        RestaurantReviewDto dto = new RestaurantReviewDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any(RestaurantReview.class))).thenReturn(savedReview);
        when(reviewMapper.mapToDto(any(RestaurantReview.class))).thenReturn(dto);
        when(reviewRepository.findByRestaurantId(2L)).thenReturn(List.of(savedReview));

        RestaurantReviewDto result = reviewService.addReview(1L, 2L, 5, "Excellent!");

        assertEquals(dto, result);
        verify(reviewRepository).save(any(RestaurantReview.class));
        verify(reviewMapper).mapToDto(any(RestaurantReview.class));
        verify(restaurantRepository, times(2)).findById(2L); // once in addReview, once in updateAverageRating
        verify(restaurantRepository).save(any(Restaurant.class));
    }
    @Test
    void addReview_shouldThrowWhenClientNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> reviewService.addReview(1L, 2L, 5, "Nice"));

        assertEquals("Client not found", ex.getMessage());
    }
    @Test
    void addReview_shouldThrowWhenRestaurantNotFound() {
        User client = new User();
        setId(client, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> reviewService.addReview(1L, 2L, 5, "Nice"));

        assertEquals("Restaurant not found", ex.getMessage());
    }
    @Test
    void getReviewsForRestaurant_shouldReturnMappedDtos() {
        Long restaurantId = 2L;
        RestaurantReview review1 = new RestaurantReview();
        RestaurantReview review2 = new RestaurantReview();
        RestaurantReviewDto dto1 = new RestaurantReviewDto();
        RestaurantReviewDto dto2 = new RestaurantReviewDto();

        setId(review1, 1L);
        setId(review2, 2L);

        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(review1, review2));
        when(reviewMapper.mapToDto(review1)).thenReturn(dto1);
        when(reviewMapper.mapToDto(review2)).thenReturn(dto2);

        List<RestaurantReviewDto> result = reviewService.getReviewsForRestaurant(restaurantId);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(reviewRepository).findByRestaurantId(restaurantId);
        verify(reviewMapper).mapToDto(review1);
        verify(reviewMapper).mapToDto(review2);
    }
}
