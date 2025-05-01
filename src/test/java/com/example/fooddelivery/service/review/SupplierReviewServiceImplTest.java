package com.example.fooddelivery.service.review;

import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.entity.review.SupplierReview;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.exception.review.SameClientAndSupplierException;
import com.example.fooddelivery.exception.role.InvalidRoleException;
import com.example.fooddelivery.mapper.review.ReviewMapper;
import com.example.fooddelivery.repository.SupplierReviewRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierReviewServiceImplTest {
    @Mock
    private SupplierReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private SupplierReviewServiceImpl reviewService;

    @Test
    void addReview_shouldSaveReviewAndReturnDto_whenValidInput() {
        Long clientId = 1L;
        Long supplierId = 2L;
        int rating = 5;
        String comment = "Great!";

        User client = new User();
        ReflectionTestUtils.setField(client, "id", clientId);

        User supplier = new User();
        ReflectionTestUtils.setField(supplier, "id", supplierId);
        Role role = new Role();
        role.setName("SUPPLIER");
        supplier.setRole(role);

        SupplierReview savedReview = new SupplierReview();
        SupplierReviewDto dto = new SupplierReviewDto();

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(reviewRepository.save(any(SupplierReview.class))).thenReturn(savedReview);
        when(reviewMapper.mapToDto(savedReview)).thenReturn(dto);

        SupplierReviewDto result = reviewService.addReview(clientId, supplierId, rating, comment);

        assertEquals(dto, result);
        verify(userRepository).findById(supplierId);
        verify(userRepository).findById(clientId);
        verify(reviewRepository).save(any(SupplierReview.class));
        verify(reviewMapper).mapToDto(savedReview);
    }
    @Test
    void addReview_shouldThrow_whenSupplierNotFound() {
        Long supplierId = 99L;
        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> reviewService.addReview(1L, supplierId, 5, "Test"));

        assertEquals("Supplier not found.", ex.getMessage());
    }
    @Test
    void addReview_shouldThrow_whenSupplierIsNotASupplier() {
        Long supplierId = 2L;
        Long clientId = 1L;

        User supplier = new User();
        ReflectionTestUtils.setField(supplier, "id", supplierId);
        Role role = new Role();
        role.setName("CLIENT");
        supplier.setRole(role);

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        InvalidRoleException ex = assertThrows(InvalidRoleException.class,
                () -> reviewService.addReview(clientId, supplierId, 5, "Bad"));

        assertEquals("Reviews are only possible for suppliers.", ex.getMessage());
    }
    @Test
    void addReview_shouldThrow_whenClientReviewsThemselves() {
        Long id = 5L;

        User supplier = new User();
        ReflectionTestUtils.setField(supplier, "id", id);
        Role role = new Role();
        role.setName("SUPPLIER");
        supplier.setRole(role);

        when(userRepository.findById(id)).thenReturn(Optional.of(supplier));

        SameClientAndSupplierException ex = assertThrows(SameClientAndSupplierException.class,
                () -> reviewService.addReview(id, id, 4, "Self-review"));

        assertEquals("No permission to review yourself.", ex.getMessage());
    }
    @Test
    void addReview_shouldThrow_whenClientNotFound() {
        Long clientId = 1L;
        Long supplierId = 2L;

        User supplier = new User();
        ReflectionTestUtils.setField(supplier, "id", supplierId);
        Role role = new Role();
        role.setName("SUPPLIER");
        supplier.setRole(role);

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(userRepository.findById(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> reviewService.addReview(clientId, supplierId, 5, "Test"));

        assertEquals("User not found.", ex.getMessage());
    }
    @Test
    void getReviewsForSupplier_shouldReturnMappedDto() throws Exception {
        Long supplierId = 1L;

        // Review 1
        SupplierReview review1 = new SupplierReview();
        User reviewer1 = new User();
        setField(reviewer1, "id", 1001L);
        review1.setReviewer(reviewer1);
        review1.setRating(5);
        review1.setComment("Excellent");
        setField(review1, "createdAt", LocalDateTime.now());
        setField(review1, "id", 101L);

        // Review 2
        SupplierReview review2 = new SupplierReview();
        User reviewer2 = new User();
        setField(reviewer2, "id", 1002L);
        review2.setReviewer(reviewer2);
        review2.setRating(3);
        review2.setComment("Okay");
        setField(review2, "createdAt", LocalDateTime.now());
        setField(review2, "id", 102L);

        // DTOs
        SupplierReviewDto dto1 = new SupplierReviewDto();
        dto1.setId(101L);
        dto1.setReviewerId(1001L);
        dto1.setRating(5);
        dto1.setComment("Excellent");

        SupplierReviewDto dto2 = new SupplierReviewDto();
        dto2.setId(102L);
        dto2.setReviewerId(1002L);
        dto2.setRating(3);
        dto2.setComment("Okay");

        when(reviewRepository.findBySupplierId(supplierId)).thenReturn(List.of(review1, review2));
        when(reviewMapper.mapToDto(review1)).thenReturn(dto1);
        when(reviewMapper.mapToDto(review2)).thenReturn(dto2);

        // Act
        List<SupplierReviewDto> result = reviewService.getReviewsForSupplier(supplierId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(dto1.getId(), result.get(0).getId());
        assertEquals(dto2.getId(), result.get(1).getId());

        verify(reviewRepository).findBySupplierId(supplierId);
        verify(reviewMapper).mapToDto(review1);
        verify(reviewMapper).mapToDto(review2);
    }
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy.");
    }

}
