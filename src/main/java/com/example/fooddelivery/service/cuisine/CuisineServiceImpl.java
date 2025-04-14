package com.example.fooddelivery.service.cuisine;

import com.example.fooddelivery.config.cuisine.CuisineMapper;
import com.example.fooddelivery.dto.cuisine.CuisineDto;
import com.example.fooddelivery.repository.CuisineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;
    private final CuisineMapper cuisineMapper;

    public CuisineServiceImpl(CuisineRepository cuisineRepository, CuisineMapper cuisineMapper) {
        this.cuisineRepository = cuisineRepository;
        this.cuisineMapper = cuisineMapper;
    }

    @Override
    public List<CuisineDto> getAllCuisines() {
        return cuisineRepository.findAll().stream()
                .map(cuisineMapper::mapToDto)
                .toList();
    }
}