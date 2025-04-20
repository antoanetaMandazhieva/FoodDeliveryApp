package com.example.fooddelivery.config.cuisine;

import com.example.fooddelivery.dto.cuisine.CuisineDto;
import com.example.fooddelivery.entity.cuisine.Cuisine;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CuisineMapper {

    private final ModelMapper mapper;

    public CuisineMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CuisineDto mapToDto(Cuisine cuisine) {
        return mapper.map(cuisine, CuisineDto.class);
    }

    public Cuisine mapToEntity(CuisineDto dto) {
        return mapper.map(dto, Cuisine.class);
    }
}