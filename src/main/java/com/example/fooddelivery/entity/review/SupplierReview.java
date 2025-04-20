package com.example.fooddelivery.entity.review;

import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.entity.id_mapped_superclass.IdEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import static com.example.fooddelivery.util.Messages.INVALID_RATING;


@Entity
@Table(name = "supplier_reviews")
public class SupplierReview extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private User supplier;

    @Column(nullable = false)
    private int rating;

    @Basic
    private String comment;

    @Column(name = "review_time")
    private LocalDateTime createdAt;

    public SupplierReview() {}

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getSupplier() {
        return supplier;
    }

    public void setSupplier(User supplier) {
        this.supplier = supplier;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException(INVALID_RATING);
        }

        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}