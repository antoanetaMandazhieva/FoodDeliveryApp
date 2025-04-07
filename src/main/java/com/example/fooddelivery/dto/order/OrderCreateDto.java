package com.example.fooddelivery.dto.order;

import java.util.Set;

public class OrderCreateDto {

    private Long restaurantId;
    private Set<Long> productIds;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Set<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Long> productIds) {
        this.productIds = productIds;
    }
}