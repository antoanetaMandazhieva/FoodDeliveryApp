package com.example.fooddelivery.service.review;

import com.example.fooddelivery.config.review.ReviewMapper;
import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.entity.review.SupplierReview;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.SupplierReviewRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

        SupplierReview review = new SupplierReview();
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

        assertEquals("Supplier not found", ex.getMessage());
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

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> reviewService.addReview(clientId, supplierId, 5, "Bad"));

        assertEquals("Reviews are only possible for suppliers", ex.getMessage());
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

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> reviewService.addReview(id, id, 4, "Self-review"));

        assertEquals("No permission to review yourself", ex.getMessage());
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

        assertEquals("Client not found", ex.getMessage());
    }
   /* @Test
    void getReviewsForSupplier_shouldReturnMappedDto() {
        Long supplierId = 10L;

        SupplierReview review1 = new SupplierReview();
        SupplierReview review2 = new SupplierReview();

        SupplierReviewDto dto1 = new SupplierReviewDto();
        dto1.setId(1L);
        dto1.setReviewerId(100L);
        dto1.setRating(5);
        dto1.setComment("Great service");

        SupplierReviewDto dto2 = new SupplierReviewDto();
        dto2.setId(2L);
        dto2.setReviewerId(101L);
        dto2.setRating(4);
        dto2.setComment("Good overall");

        when(reviewRepository.findBySupplierId(supplierId)).thenReturn(List.of(review1, review2));
        when(reviewMapper.mapToDto(review1)).thenReturn(dto1); // важен ред
        when(reviewMapper.mapToDto(review2)).thenReturn(dto2); // важен ред

        List<SupplierReviewDto> result = reviewService.getReviewsForSupplier(supplierId);

        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(dto ->
                Long.valueOf(1L).equals(dto.getId()) &&
                        Long.valueOf(100L).equals(dto.getReviewerId()) &&
                        dto.getRating() == 5 &&
                        "Great service".equals(dto.getComment())
        ), "Expected dto1 not found");

        assertTrue(result.stream().anyMatch(dto ->
                Long.valueOf(2L).equals(dto.getId()) &&
                        Long.valueOf(101L).equals(dto.getReviewerId()) &&
                        dto.getRating() == 4 &&
                        "Good overall".equals(dto.getComment())
        ), "Expected dto2 not found");
    }*/
}
