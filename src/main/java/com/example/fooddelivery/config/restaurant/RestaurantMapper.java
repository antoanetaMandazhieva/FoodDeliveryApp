package com.example.fooddelivery.config.restaurant;

import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.Cuisine;
import com.example.fooddelivery.entity.Restaurant;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class RestaurantMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Restaurant mapToEntity(RestaurantCreateDto dto, Set<Cuisine> cuisines) {
        Restaurant restaurant = mapper.map(dto, Restaurant.class);

        for (Cuisine cuisine : cuisines) {
            restaurant.addCuisine(cuisine);
        }

        return restaurant;
    }

    public static RestaurantDto mapToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto = mapper.map(restaurant, RestaurantDto.class);

        restaurantDto.setCuisineIds(
                restaurant.getCuisines().stream()
                        .map(Cuisine::getId)
                        .collect(Collectors.toSet())
        );

        return restaurantDto;
    }
}