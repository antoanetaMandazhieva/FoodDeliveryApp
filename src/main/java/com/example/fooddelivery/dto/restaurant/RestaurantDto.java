package com.example.fooddelivery.dto.restaurant;

import com.example.fooddelivery.dto.address.AddressDto;

import java.math.BigDecimal;
import java.util.Set;

public class RestaurantDto {

    private Long id;
    private String name;
    private AddressDto address;
    private Set<Long> cuisineIds;
    private BigDecimal averageRating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public Set<Long> getCuisineIds() {
        return cuisineIds;
    }

    public void setCuisineIds(Set<Long> cuisineIds) {
        this.cuisineIds = cuisineIds;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }
}
