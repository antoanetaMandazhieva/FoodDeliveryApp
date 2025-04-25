package com.example.fooddelivery.controller.review;

import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.service.review.SupplierReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SupplierReviewControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SupplierReviewService reviewService;

    @InjectMocks
    private SupplierReviewController reviewController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void testAddReview_ShouldReturnCreatedReview() throws Exception {
        Long clientId = 1L;
        Long supplierId = 2L;
        int rating = 5;
        String comment = "Excellent!";
        SupplierReviewDto savedReview = new SupplierReviewDto();
        savedReview.setId(100L);

        when(reviewService.addReview(eq(clientId), eq(supplierId), eq(rating), eq(comment)))
                .thenReturn(savedReview);

        mockMvc.perform(post("/api/reviews/suppliers/{supplierId}/add", supplierId)
                        .param("clientId", clientId.toString())
                        .param("rating", String.valueOf(rating))
                        .param("comment", comment))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/reviews/suppliers/100"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void testGetReviews_ShouldReturnReviewList() throws Exception {
        Long supplierId = 2L;
        SupplierReviewDto review1 = new SupplierReviewDto();
        SupplierReviewDto review2 = new SupplierReviewDto();
        when(reviewService.getReviewsForSupplier(supplierId)).thenReturn(List.of(review1, review2));

        mockMvc.perform(get("/api/reviews/suppliers/{supplierId}", supplierId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
