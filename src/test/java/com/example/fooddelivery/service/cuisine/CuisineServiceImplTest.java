package com.example.fooddelivery.service.cuisine;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.fooddelivery.config.cuisine.CuisineMapper;
import com.example.fooddelivery.dto.cuisine.CuisineDto;
import com.example.fooddelivery.entity.cuisine.Cuisine;
import com.example.fooddelivery.repository.CuisineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CuisineServiceImplTest {

    @Mock
    private CuisineRepository cuisineRepository;

    @Mock
    private CuisineMapper cuisineMapper;

    @InjectMocks
    private CuisineServiceImpl cuisineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    // Проверява ако в БД има две cuisine entity-та дали се мапват правилно към DTO-то
    @Test
    void getAllCuisines_shouldReturnListOfCuisineDto_whenCuisinesExist() {
        Cuisine cuisine1 = new Cuisine();
        ReflectionTestUtils.setField(cuisine1, "id", 1L);
        Cuisine cuisine2 = new Cuisine();
        ReflectionTestUtils.setField(cuisine2, "id", 2L);
        CuisineDto dto1 = new CuisineDto();
        dto1.setId(1L);
        dto1.setName("Italian");
        CuisineDto dto2 = new CuisineDto();
        dto2.setId(2L);
        dto2.setName("Chinese");

        when(cuisineRepository.findAll()).thenReturn(List.of(cuisine1, cuisine2));
        when(cuisineMapper.mapToDto(cuisine1)).thenReturn(dto1);
        when(cuisineMapper.mapToDto(cuisine2)).thenReturn(dto2);

        List<CuisineDto> result = cuisineService.getAllCuisines();

        assertThat(result)
                .extracting(CuisineDto::getId)
                .containsExactlyInAnyOrder(1L, 2L);

        verify(cuisineRepository).findAll();
        verify(cuisineMapper).mapToDto(cuisine1);
        verify(cuisineMapper).mapToDto(cuisine2);
    }

    // Проверява дали ако в БД няма нищо ще върне празен списък
    @Test
    void getAllCuisines_shouldReturnEmptyList_whenNoCuisinesExist() {
        when(cuisineRepository.findAll()).thenReturn(List.of());
        List<CuisineDto> result = cuisineService.getAllCuisines();

        assertTrue(result.isEmpty());
        verify(cuisineRepository).findAll();
        verifyNoInteractions(cuisineMapper);
    }
}
