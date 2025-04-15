package com.example.fooddelivery.dto.order;

import com.example.fooddelivery.dto.address.AddressDto;

import java.util.Set;

public class OrderCreateDto {

    private Long restaurantId;
    private Set<Long> productIds;
    private AddressDto address;

    public OrderCreateDto() {}

    public OrderCreateDto(Long restaurantId, Set<Long> productIds, AddressDto address) {
        this.restaurantId = restaurantId;
        this.productIds = productIds;
        this.address = address;
    }

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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}