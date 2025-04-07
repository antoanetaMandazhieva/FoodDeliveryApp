package com.example.fooddelivery.dto.restaurant;

import com.example.fooddelivery.dto.address.AddressDto;

import java.util.Set;

public class RestaurantCreateDto {

    private String name;
    private AddressDto address;
    private Set<Long> cuisineIds;

    public RestaurantCreateDto() {}

    public RestaurantCreateDto(String name, AddressDto address) {
        this.name = name;
        this.address = address;
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
}