package com.example.fooddelivery.config.restaurant;

import com.example.fooddelivery.config.cuisine.CuisineMapper;
import com.example.fooddelivery.config.product.ProductMapper;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.cuisine.Cuisine;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    private final ModelMapper mapper;
    private final CuisineMapper cuisineMapper;
    private final ProductMapper productMapper;

    public RestaurantMapper(ModelMapper mapper, CuisineMapper cuisineMapper, ProductMapper productMapper) {
        this.mapper = mapper;
        this.cuisineMapper = cuisineMapper;
        this.productMapper = productMapper;
    }

    public Restaurant mapToEntity(RestaurantCreateDto dto, Set<Cuisine> cuisines) {
        Restaurant restaurant = mapper.map(dto, Restaurant.class);

        for (Cuisine cuisine : cuisines) {
            restaurant.addCuisine(cuisine);
        }

        return restaurant;
    }

    public RestaurantDto mapToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto = mapper.map(restaurant, RestaurantDto.class);

        restaurantDto.setCuisineDtos(
                restaurant.getCuisines().stream()
                        .map(cuisineMapper::mapToDto)
                        .collect(Collectors.toSet())
        );

        restaurantDto.setProducts(
                restaurant.getProducts().stream()
                        .map(productMapper::mapToProductDto)
                        .collect(Collectors.toSet())
        );

        return restaurantDto;
    }
}