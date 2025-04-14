package com.example.fooddelivery.service.restaurant;

import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;

import java.util.List;

public interface RestaurantService {

    RestaurantDto getRestaurantByName(String restaurantName);

    List<ProductDto> getAllAvailableProductsFromRestaurant(String restaurantName);

    ProductDto getProductFromRestaurantByName(String restaurantName, String productName);

    RestaurantDto createRestaurant(RestaurantCreateDto dto, Long employeeId);

    void addProductToRestaurant(Long employeeId, Long restaurantId, Long productId);

    void removeProductFromRestaurant(Long employeeId, Long restaurantId, Long productId);

    List<RestaurantDto> getRestaurantsByCuisine(Long cuisineId);

    List<RestaurantDto> getTopRatedRestaurants(int limit);

    List<RestaurantDto> getRestaurantsByNameAsc();

    List<RestaurantDto> getRestaurantsByNameDesc();
}