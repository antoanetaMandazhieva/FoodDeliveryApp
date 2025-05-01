package com.example.fooddelivery.mapper.review;

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
        // Създаване и настройка на обект SupplierReview
        SupplierReview review = new SupplierReview();
        setField(review, "id", 1L); // Използваме reflection, за да зададем полето 'id'
        review.setRating(5);
        review.setComment("Excellent service");

        // Създаване и настройка на обект User
        User reviewer = new User();
        setField(reviewer, "id", 100L);  // Използваме reflection, за да зададем полето 'id'
        review.setReviewer(reviewer);

        // Мапване на SupplierReview към SupplierReviewDto
        SupplierReviewDto dto = reviewMapper.mapToDto(review);

        ///Предявяваме мапването е успешно и всички полета са коректно прехвърлени
        assertNotNull(dto);
        assertEquals(1L, dto.getId()); // Проверяваме дали 'id' е мапнато правилно
        assertEquals(100L, dto.getReviewerId());
        assertEquals(5, dto.getRating());
        assertEquals("Excellent service", dto.getComment());
    }
    @Test
    void mapToRestaurantReviewDto_whenValidReview_shouldMapAllFieldsCorrectly() {
        RestaurantReview review = new RestaurantReview();
        setField(review, "id", 2L); // Използваме reflection, за да зададем полето 'id'
        review.setRating(4);
        review.setComment("Very good food");

        User reviewer = new User();
        setField(reviewer, "id", 200L); // Използваме reflection, за да зададем полето 'id'
        review.setReviewer(reviewer);

        // Мапване на RestaurantReview към RestaurantReviewDto
        RestaurantReviewDto dto = reviewMapper.mapToDto(review);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(200L, dto.getReviewerId());
        assertEquals(4, dto.getRating());
        assertEquals("Very good food", dto.getComment());
    }
    @Test
    void mapToSupplierReviewDto_whenReviewerIsNull_shouldThrowException() {
        // Създаване на обект SupplierReview с null reviewer
        SupplierReview review = new SupplierReview();
        setField(review, "id", 3L);
        review.setReviewer(null);
        // Очакваме да бъде хвърлена NullPointerException, когато се опитаме да мапнем
        assertThrows(NullPointerException.class, () -> reviewMapper.mapToDto(review));
    }
    @Test
    void mapToRestaurantReviewDto_whenReviewerIsNull_shouldThrowException() {
        // Създаване на обект SupplierReview с null review
        RestaurantReview review = new RestaurantReview();
        setField(review, "id", 4L);
        review.setReviewer(null);

        // Очакваме да бъде хвърлена NullPointerException, когато се опитаме да мапнем
        assertThrows(NullPointerException.class, () -> reviewMapper.mapToDto(review));
    }
    @Test
    void mapToSupplierReviewDto_whenIdAndReviewerIdAlreadySet_shouldNotOverrideThem() {
        SupplierReview review = new SupplierReview();
        setField(review, "id", 5L);
        review.setRating(3);
        review.setComment("Average");

        User reviewer = new User();
        setField(reviewer, "id", 500L); // Използваме reflection, за да зададем полето 'id' за рецензента
        review.setReviewer(reviewer);

        // Създаване на съществуващ SupplierReviewDto с предварително зададени ID и reviewerId
        SupplierReviewDto dto = new SupplierReviewDto();
        setField(dto, "id", 99L);          // Existing ID
        setField(dto, "reviewerId", 999L); // Existing ReviewerId

        // Принудително мапваме ръчно, за да проверим дали if логиката не презаписва вече зададени стойности
        SupplierReviewDto mappedDto = reviewMapper.mapToDto(review);

        assertNotNull(mappedDto);
        assertEquals(5L, mappedDto.getId()); // Уверяваме се, че 'id' остава 5L
        assertEquals(500L, mappedDto.getReviewerId()); // Уверяваме се, че 'reviewerId' остава 500L
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

        // Създаване на съществуващ RestaurantReviewDto с предварително зададени ID и reviewerId
        RestaurantReviewDto dto = new RestaurantReviewDto();
        setField(dto, "id", 88L);
        setField(dto, "reviewerId", 888L);

        // Мапване на рецензията към DTO
        RestaurantReviewDto mappedDto = reviewMapper.mapToDto(review);

        assertNotNull(mappedDto);
        assertEquals(6L, mappedDto.getId());  // Уверяваме се, че 'id' остава 6L
        assertEquals(600L, mappedDto.getReviewerId());  // Уверяваме се, че 'reviewerId' остава 600L
    }
    // Помощен метод за задаване на стойности на полета чрез reflection
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
