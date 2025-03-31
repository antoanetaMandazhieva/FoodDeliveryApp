package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static util.Messages.INVALID_RATING;


@Entity
@Table(name = "restaurant_reviews")
public class RestaurantReview extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(nullable = false)
    private int rating;

    @Column(name = "review_time")
    private LocalDateTime reviewTime;

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

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }


    @PrePersist
    protected void onCreate() {
        this.reviewTime = LocalDateTime.now();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}