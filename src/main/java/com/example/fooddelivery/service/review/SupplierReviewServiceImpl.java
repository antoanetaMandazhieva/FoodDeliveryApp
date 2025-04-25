package com.example.fooddelivery.service.review;

import com.example.fooddelivery.config.review.ReviewMapper;
import com.example.fooddelivery.dto.review.SupplierReviewDto;
import com.example.fooddelivery.entity.review.SupplierReview;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.exception.review.SameClientAndSupplierException;
import com.example.fooddelivery.exception.role.InvalidRoleException;
import com.example.fooddelivery.repository.SupplierReviewRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.fooddelivery.util.SystemErrors.Review.REVIEW_ONLY_FOR_SUPPLIER;
import static com.example.fooddelivery.util.SystemErrors.Review.REVIEW_UNABLE_TO_YOURSELF;
import static com.example.fooddelivery.util.SystemErrors.User.SUPPLIER_NOT_FOUND;
import static com.example.fooddelivery.util.SystemErrors.User.USER_NOT_FOUND;

@Service
public class SupplierReviewServiceImpl implements SupplierReviewService {

    private static final String SUPPLIER_ROLE = "SUPPLIER";

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
    @Transactional
    public SupplierReviewDto addReview(Long clientId, Long supplierId, int rating, String comment) {
        User supplier = getSupplier(supplierId);

        validateSupplier(supplier);

        if (clientId.equals(supplierId)) {
            throw new SameClientAndSupplierException(REVIEW_UNABLE_TO_YOURSELF);
        }

        User client = getUser(clientId);

        SupplierReview review = createReview(client, supplier, rating, comment);
        SupplierReview saved = reviewRepository.save(review);

        return reviewMapper.mapToDto(saved);
    }

    @Override
    @Transactional
    public List<SupplierReviewDto> getReviewsForSupplier(Long supplierId) {
        return reviewRepository.findBySupplierId(supplierId)
                .stream()
                .map(reviewMapper::mapToDto)
                .toList();
    }

    private User getSupplier(Long supplierId) {
        return userRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException(SUPPLIER_NOT_FOUND));
    }

    private void validateSupplier(User supplier) {
        if (!SUPPLIER_ROLE.equals(supplier.getRole().getName())) {
            throw new InvalidRoleException(REVIEW_ONLY_FOR_SUPPLIER);
        }
    }

    private User getUser(Long clientId) {
        return userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private SupplierReview createReview(User client, User supplier, int rating, String comment) {
        SupplierReview review = new SupplierReview();

        review.setReviewer(client);
        review.setSupplier(supplier);
        review.setRating(rating);
        review.setComment(comment);

        return review;
    }
}
