package com.example.fooddelivery.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static com.example.fooddelivery.util.Messages.INVALID_RATING;


@Entity
@Table(name = "supplier_reviews")
public class SupplierReview extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(nullable = false)
    private int rating;

    @Column(name = "review_time")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private User supplier;

    public SupplierReview() {}

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public User getSupplier() {
        return supplier;
    }

    public void setSupplier(User supplier) {
        this.supplier = supplier;
    }
}