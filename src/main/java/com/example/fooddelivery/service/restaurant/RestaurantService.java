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

    RestaurantDto addProductsToRestaurant(Long employeeId, Long restaurantId, List<ProductDto> productDtos);

    RestaurantDto removeProductFromRestaurant(Long employeeId, Long restaurantId, Long productId);

    List<RestaurantDto> getRestaurantsByCuisine(Long cuisineId);

    List<RestaurantDto> getTopRatedRestaurants();

    List<RestaurantDto> getRestaurantsByNameAsc();

    List<RestaurantDto> getRestaurantsByNameDesc();

    List<RestaurantDto> getRestaurantByPartName(String partName);
}