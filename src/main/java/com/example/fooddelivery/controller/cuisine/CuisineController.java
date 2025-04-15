package com.example.fooddelivery.controller.cuisine;

import com.example.fooddelivery.dto.cuisine.CuisineDto;
import com.example.fooddelivery.service.cuisine.CuisineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cuisines")
public class CuisineController {

    private final CuisineService cuisineService;

    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @GetMapping
    public ResponseEntity<List<CuisineDto>> getAllCuisines() {
        List<CuisineDto> cuisines = cuisineService.getAllCuisines();
        return ResponseEntity.ok(cuisines);
    }


}