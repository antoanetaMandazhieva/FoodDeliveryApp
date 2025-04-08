package com.example.fooddelivery.service.restaurant;

import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.Product;
import com.example.fooddelivery.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant getRestaurantByName(String restaurantName);

    List<Product> getAllAvailableProductsFromRestaurant(String restaurantName);

    RestaurantDto createRestaurant(RestaurantCreateDto dto, Long employeeId);

    void addProductToRestaurant(Long restaurantId, Long productId);

    void removeProductFromRestaurant(Long restaurantId, Long productId);

    List<RestaurantDto> getRestaurantsByCuisine(Long cuisineId);

    public List<Restaurant> getTopRatedRestaurants(int limit);
}