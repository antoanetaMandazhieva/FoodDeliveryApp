package com.example.fooddelivery.controller.cuisine;

import com.example.fooddelivery.dto.cuisine.CuisineDto;
import com.example.fooddelivery.service.cuisine.CuisineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuisineController.class)
class CuisineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuisineService cuisineService;

    //Тества дали контролерът връща списък с наличните кухни и дали полетата им са коректни
    @Test
    void getAllCuisines_shouldReturnListOfCuisines() throws Exception {
        CuisineDto cuisine1 = new CuisineDto();
        cuisine1.setName("Italian");

        CuisineDto cuisine2 = new CuisineDto();
        cuisine2.setName("Mexican");

        List<CuisineDto> cuisines = List.of(cuisine1, cuisine2);

        when(cuisineService.getAllCuisines()).thenReturn(cuisines);

        mockMvc.perform(get("/api/cuisines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(cuisines.size()))
                .andExpect(jsonPath("$[0].name").value("Italian"))
                .andExpect(jsonPath("$[1].name").value("Mexican"));
    }
    //Тества дали контролерът връща празен списък, когато няма налични кухни
    @Test
    void getAllCuisines_shouldReturnEmptyList_whenNoCuisinesExist() throws Exception {
        when(cuisineService.getAllCuisines()).thenReturn(List.of());

        mockMvc.perform(get("/api/cuisines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}
