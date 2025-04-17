package com.example.fooddelivery.config.restaurant;

import com.example.fooddelivery.config.cuisine.CuisineMapper;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.Cuisine;
import com.example.fooddelivery.entity.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    private final ModelMapper mapper;
    private final CuisineMapper cuisineMapper;

    public RestaurantMapper(ModelMapper mapper, CuisineMapper cuisineMapper) {
        this.mapper = mapper;
        this.cuisineMapper = cuisineMapper;
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

        return restaurantDto;
    }
}