package com.example.fooddelivery.service.review;

import com.example.fooddelivery.config.review.ReviewMapper;
import com.example.fooddelivery.dto.review.ReviewDto;
import com.example.fooddelivery.entity.SupplierReview;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.repository.SupplierReviewRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierReviewServiceImpl implements SupplierReviewService {

    private final SupplierReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public SupplierReviewServiceImpl(SupplierReviewRepository reviewRepository,
                                     UserRepository userRepository,
                                     ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public void addReview(Long clientId, Long supplierId, int rating, String comment) {

        if (clientId.equals(supplierId)) {
            throw new IllegalStateException("No permission to review yourself");
        }

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        if (!"SUPPLIER".equals(supplier.getRole().getName())) {
            throw new IllegalStateException("Reviews are only possible for suppliers");
        }


        SupplierReview review = new SupplierReview();
        review.setReviewer(client);
        review.setSupplier(supplier);
        review.setRating(rating);
        review.setComment(comment);

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewDto> getReviewsForSupplier(Long supplierId) {
        return reviewRepository.findBySupplierId(supplierId)
                .stream()
                .map(reviewMapper::mapToDto)
                .toList();
    }
}
