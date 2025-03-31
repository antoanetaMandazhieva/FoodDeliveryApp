package com.example.fooddelivery.entity;

import com.example.fooddelivery.enums.Category;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Products")
public class Product extends IdEntity {

    @Column(length = 50, nullable = false)
    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isAvailable;

    public Product() {
        this.price = BigDecimal.ZERO;
        this.isAvailable = true;
    }

    public Product(String name, BigDecimal price, Category category, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
        this.isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}