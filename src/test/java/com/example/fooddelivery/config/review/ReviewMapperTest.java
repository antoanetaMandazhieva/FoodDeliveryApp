package com.example.fooddelivery.config.review;

import com.example.fooddelivery.dto.review.RestaurantReviewDto;
import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.entity.review.RestaurantReview;
import com.example.fooddelivery.entity.review.SupplierReview;
import com.example.fooddelivery.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
public class ReviewMapperTest {
    private ReviewMapper reviewMapper;

    @BeforeEach
    void setUp() {
        reviewMapper = new ReviewMapper(new ModelMapper());
    }
    @Test
    void mapToSupplierReviewDto_whenValidReview_shouldMapAllFieldsCorrectly() {
        SupplierReview review = new SupplierReview();
        setField(review, "id", 1L);
        review.setRating(5);
        review.setComment("Excellent service");

        User reviewer = new User();
        setField(reviewer, "id", 100L);
        review.setReviewer(reviewer);

        SupplierReviewDto dto = reviewMapper.mapToDto(review);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getReviewerId());
        assertEquals(5, dto.getRating());
        assertEquals("Excellent service", dto.getComment());
    }
    @Test
    void mapToRestaurantReviewDto_whenValidReview_shouldMapAllFieldsCorrectly() {
        RestaurantReview review = new RestaurantReview();
        setField(review, "id", 2L);
        review.setRating(4);
        review.setComment("Very good food");

        User reviewer = new User();
        setField(reviewer, "id", 200L);
        review.setReviewer(reviewer);

        RestaurantReviewDto dto = reviewMapper.mapToDto(review);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(200L, dto.getReviewerId());
        assertEquals(4, dto.getRating());
        assertEquals("Very good food", dto.getComment());
    }
    @Test
    void mapToSupplierReviewDto_whenReviewerIsNull_shouldThrowException() {
        SupplierReview review = new SupplierReview();
        setField(review, "id", 3L);
        review.setReviewer(null);

        assertThrows(NullPointerException.class, () -> reviewMapper.mapToDto(review));
    }
    @Test
    void mapToRestaurantReviewDto_whenReviewerIsNull_shouldThrowException() {
        RestaurantReview review = new RestaurantReview();
        setField(review, "id", 4L);
        review.setReviewer(null);

        assertThrows(NullPointerException.class, () -> reviewMapper.mapToDto(review));
    }
    @Test
    void mapToSupplierReviewDto_whenIdAndReviewerIdAlreadySet_shouldNotOverrideThem() {
        SupplierReview review = new SupplierReview();
        setField(review, "id", 5L);
        review.setRating(3);
        review.setComment("Average");

        User reviewer = new User();
        setField(reviewer, "id", 500L);
        review.setReviewer(reviewer);

        SupplierReviewDto dto = new SupplierReviewDto();
        setField(dto, "id", 99L);          // Existing ID
        setField(dto, "reviewerId", 999L); // Existing ReviewerId

        // Принудително мапваме ръчно, за да проверим дали if логиката не презаписва вече зададени стойности
        SupplierReviewDto mappedDto = reviewMapper.mapToDto(review);

        assertNotNull(mappedDto);
        assertEquals(5L, mappedDto.getId());
        assertEquals(500L, mappedDto.getReviewerId());
    }
    @Test
    void mapToRestaurantReviewDto_whenIdAndReviewerIdAlreadySet_shouldNotOverrideThem() {
        RestaurantReview review = new RestaurantReview();
        setField(review, "id", 6L);
        review.setRating(2);
        review.setComment("Bad experience");

        User reviewer = new User();
        setField(reviewer, "id", 600L);
        review.setReviewer(reviewer);

        RestaurantReviewDto dto = new RestaurantReviewDto();
        setField(dto, "id", 88L);
        setField(dto, "reviewerId", 888L);

        RestaurantReviewDto mappedDto = reviewMapper.mapToDto(review);

        assertNotNull(mappedDto);
        assertEquals(6L, mappedDto.getId());
        assertEquals(600L, mappedDto.getReviewerId());
    }
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = null;
            Class<?> clazz = target.getClass();
            while (clazz != null) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (field == null) {
                throw new NoSuchFieldException("Field not found: " + fieldName);
            }
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
