package com.example.fooddelivery.dto.order;

import com.example.fooddelivery.dto.address.AddressDto;

import java.util.Set;

public class OrderCreateDto {

    private Long restaurantId;
    private Set<OrderProductDto> products;
    private AddressDto address;

    public OrderCreateDto() {}

    public OrderCreateDto(Long restaurantId, Set<OrderProductDto> productIds, AddressDto address) {
        this.restaurantId = restaurantId;
        this.products = productIds;
        this.address = address;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Set<OrderProductDto> getProducts() {
        return products;
    }

    public void setProducts(Set<OrderProductDto> productIds) {
        this.products = productIds;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}