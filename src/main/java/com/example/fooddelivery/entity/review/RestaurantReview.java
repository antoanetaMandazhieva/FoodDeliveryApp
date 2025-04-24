package com.example.fooddelivery.entity.review;

import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.entity.id_mapped_superclass.IdEntity;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import static com.example.fooddelivery.util.SystemErrors.Review.INVALID_RATING;


@Entity
@Table(name = "restaurant_reviews")
public class RestaurantReview extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(nullable = false)
    private int rating;

    @Basic
    private String comment;

    @Column(name = "review_time")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;


    public RestaurantReview() {}


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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}