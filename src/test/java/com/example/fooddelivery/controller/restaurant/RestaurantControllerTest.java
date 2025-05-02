package com.example.fooddelivery.controller.restaurant;

import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.service.restaurant.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    //getRestaurantByName
    //При съществуващ ресторант по име, връща коректен RestaurantDto
    @Test
    void getRestaurantByName_shouldReturnRestaurant_whenRestaurantExists() throws Exception {
        String restaurantName = "Pizzeria Roma";
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName(restaurantName);

        when(restaurantService.getRestaurantByName(restaurantName)).thenReturn(restaurantDto);

        mockMvc.perform(get("/api/restaurants/name/{name}", restaurantName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(restaurantName));
    }
    //При несъществуващ ресторант по име, връща грешка
    @Test
    void getRestaurantByName_shouldReturnNotFound_whenRestaurantDoesNotExist() throws Exception {
        String restaurantName = "RandomIvanov";

        when(restaurantService.getRestaurantByName(restaurantName))
                .thenThrow(new EntityNotFoundException("Restaurant not found"));

        mockMvc.perform(get("/api/restaurants/name/{name}", restaurantName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //getRestaurantByPartName_shouldReturnListOfRestaurants_whenMatchesExist
    //При частично съвпадение на име, връща списък с ресторанти
    @Test
    void getRestaurantsByPartName_shouldReturnListOfRestaurants_whenMatchesExist() throws Exception {
        String partName = "Pizza";

        RestaurantDto restaurant1 = new RestaurantDto();
        restaurant1.setName("Pizzeria Roma");

        RestaurantDto restaurant2 = new RestaurantDto();
        restaurant2.setName("Pizza House");

        List<RestaurantDto> restaurants = List.of(restaurant1, restaurant2);

        when(restaurantService.getRestaurantsByPartName(partName)).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants/part-name/{partName}", partName) // изпраща го като path variable
                        .param("partName", partName) // изпраща го и като request параметър, защото контролерът го очаква така
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(restaurants.size()))
                .andExpect(jsonPath("$[0].name").value("Pizzeria Roma"))
                .andExpect(jsonPath("$[1].name").value("Pizza House"));
    }
    //При липса на съвпадения с част от име, връща празен списък
    @Test
    void getRestaurantsByPartName_shouldReturnEmptyList_whenNoMatchesExist() throws Exception {
        String partName = "NonExistent";

        when(restaurantService.getRestaurantsByPartName(partName)).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/part-name/{partName}", partName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    //getAllAvailableProduct
    //Тества дали връща списък с наличните продукти за ресторанта
    @Test
    void getAllAvailableProducts_shouldReturnProducts_whenRestaurantExists() throws Exception {
        String restaurantName = "Pizzeria Roma";
        ProductDto product1 = new ProductDto();
        product1.setName("Margherita");
        ProductDto product2 = new ProductDto();
        product2.setName("Pepperoni");

        List<ProductDto> products = List.of(product1, product2);

        when(restaurantService.getAllAvailableProductsFromRestaurant(restaurantName)).thenReturn(products);

        mockMvc.perform(get("/api/restaurants/{name}/products", restaurantName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(products.size()))
                .andExpect(jsonPath("$[0].name").value("Margherita"))
                .andExpect(jsonPath("$[1].name").value("Pepperoni"));
    }
    // Проверява дали връща грешка, ако ресторантът не съществува при заявка за продукти
    @Test
    void getAllAvailableProducts_shouldReturnNotFound_whenRestaurantDoesNotExist() throws Exception {
        String restaurantName = "RandomIvanov";

        when(restaurantService.getAllAvailableProductsFromRestaurant(restaurantName))
                .thenThrow(new EntityNotFoundException("Restaurant not found"));

        mockMvc.perform(get("/api/restaurants/{name}/products", restaurantName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //getProductFromRestaurant
    //Тества дали връща коректен продукт от ресторант по име на ресторант и име на продукт
    @Test
    void getProductFromRestaurant_shouldReturnProduct_whenProductExistsInRestaurant() throws Exception {
        String restaurantName = "Pizzeria Roma";
        String productName = "Margherita";
        ProductDto productDto = new ProductDto();
        productDto.setName(productName);

        when(restaurantService.getProductFromRestaurantByName(restaurantName, productName)).thenReturn(productDto);

        mockMvc.perform(get("/api/restaurants/{restaurantName}/product/{productName}", restaurantName, productName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productName));
    }
    //Проверява дали връща грешка, ако продуктът не съществува в ресторанта
    @Test
    void getProductFromRestaurant_shouldReturnNotFound_whenProductDoesNotExistInRestaurant() throws Exception {
        String restaurantName = "Pizzeria Roma";
        String productName = "MEOW";

        when(restaurantService.getProductFromRestaurantByName(restaurantName, productName))
                .thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/restaurants/{restaurantName}/product/{productName}", restaurantName, productName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //createRestaurant
    //Тества дали се създава правилно ресторанта
    @Test
    void createRestaurant_shouldReturnRestaurantDto_whenInputIsValid() throws Exception {
        Long employeeId = 1L;
        RestaurantCreateDto createDto = new RestaurantCreateDto();
        createDto.setName("New Restaurant");

        RestaurantDto expectedDto = new RestaurantDto();
        expectedDto.setName("New Restaurant");

        when(restaurantService.createRestaurant(any(RestaurantCreateDto.class), eq(employeeId)))
                .thenReturn(expectedDto);

        mockMvc.perform(post("/api/restaurants/create/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Restaurant"));
    }

    //Проверява дали връща BadRequest при грешка в създаването на ресторант
    @Test
    void createRestaurant_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        Long employeeId = 1L;
        RestaurantCreateDto invalidDto = new RestaurantCreateDto(); // Липсва име

        doThrow(new IllegalArgumentException("Invalid input"))
                .when(restaurantService).createRestaurant(any(RestaurantCreateDto.class), eq(employeeId));

        mockMvc.perform(post("/api/restaurants/create/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    //addProductsToRestaurant
    //Тества дали добавя успешно продукти към ресторант
    @Test
    void addProductsToRestaurant_shouldReturnRestaurantDto_whenInputIsValid() throws Exception {
        Long restaurantId = 1L;
        Long employeeId = 2L;
        ProductDto productDto = new ProductDto();
        productDto.setName("Margherita");
        List<ProductDto> productDtos = List.of(productDto);

        RestaurantDto expectedDto = new RestaurantDto();
        expectedDto.setName("Pizzeria Roma");

        when(restaurantService.addProductsToRestaurant(eq(employeeId), eq(restaurantId), anyList()))
                .thenReturn(expectedDto);

        mockMvc.perform(post("/api/restaurants/{restaurantId}/products/add", restaurantId)
                        .param("employeeId", String.valueOf(employeeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizzeria Roma"));
    }

    //Проверява дали връща BadRequest при грешка в добавянето на продукти
    @Test
    void addProductsToRestaurant_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        Long restaurantId = 1L;
        Long employeeId = 2L;
        List<ProductDto> invalidProducts = List.of();

        doThrow(new IllegalArgumentException("Invalid products"))
                .when(restaurantService).addProductsToRestaurant(eq(employeeId), eq(restaurantId), eq(invalidProducts));

        mockMvc.perform(post("/api/restaurants/{restaurantId}/products/add", restaurantId)
                        .param("employeeId", String.valueOf(employeeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProducts)))
                .andExpect(status().isBadRequest());
    }

    //removeProductFromRestaurant
    //Тества дали премахва успешно продукт от ресторант
    @Test
    void removeProductFromRestaurant_shouldReturnRestaurantDto_whenProductRemovedSuccessfully() throws Exception {
        Long restaurantId = 1L;
        Long productId = 10L;
        Long employeeId = 2L;

        RestaurantDto expectedDto = new RestaurantDto();
        expectedDto.setName("Pizzeria Roma");

        when(restaurantService.removeProductFromRestaurant(employeeId, restaurantId, productId))
                .thenReturn(expectedDto);

        mockMvc.perform(put("/api/restaurants/{restaurantId}/remove-product/{productId}/by/{employeeId}",
                        restaurantId, productId, employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizzeria Roma"));
    }
    //Проверява дали връща NotFound при несъществуващ ресторант или продукт
    @Test
    void removeProductFromRestaurant_shouldReturnNotFound_whenRestaurantOrProductNotFound() throws Exception {
        Long restaurantId = 1L;
        Long productId = 10L;
        Long employeeId = 2L;

        doThrow(new EntityNotFoundException("Restaurant or Product not found"))
                .when(restaurantService).removeProductFromRestaurant(employeeId, restaurantId, productId);

        mockMvc.perform(put("/api/restaurants/{restaurantId}/remove-product/{productId}/by/{employeeId}",
                        restaurantId, productId, employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //getRestaurantsByCuisine
    //Тества дали връща списък с ресторанти по дадена кухня
    @Test
    void getRestaurantsByCuisine_shouldReturnListOfRestaurants_whenCuisineExists() throws Exception {
        Long cuisineId = 5L;
        RestaurantDto restaurant1 = new RestaurantDto();
        restaurant1.setName("Pizzeria Roma");
        RestaurantDto restaurant2 = new RestaurantDto();
        restaurant2.setName("Sushi Place");

        List<RestaurantDto> restaurants = List.of(restaurant1, restaurant2);

        when(restaurantService.getRestaurantsByCuisine(cuisineId)).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants/cuisine/{cuisineId}", cuisineId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(restaurants.size()))
                .andExpect(jsonPath("$[0].name").value("Pizzeria Roma"))
                .andExpect(jsonPath("$[1].name").value("Sushi Place"));
    }
    //Проверява дали връща празен списък, когато няма ресторанти за дадена кухня
    @Test
    void getRestaurantsByCuisine_shouldReturnEmptyList_whenNoRestaurantsExist() throws Exception {
        Long cuisineId = 5L;

        when(restaurantService.getRestaurantsByCuisine(cuisineId)).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/cuisine/{cuisineId}", cuisineId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    //getTopRatedRestaurants
    //Тества дали връща списък с най-високо оценените ресторанти
    @Test
    void getTopRatedRestaurants_shouldReturnListOfRestaurants() throws Exception {
        RestaurantDto restaurant1 = new RestaurantDto();
        restaurant1.setName("Gourmet Palace");
        RestaurantDto restaurant2 = new RestaurantDto();
        restaurant2.setName("Elite Sushi");

        List<RestaurantDto> restaurants = List.of(restaurant1, restaurant2);

        when(restaurantService.getTopRatedRestaurants()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants/top-rated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(restaurants.size()))
                .andExpect(jsonPath("$[0].name").value("Gourmet Palace"))
                .andExpect(jsonPath("$[1].name").value("Elite Sushi"));
    }
    //Проверява дали връща празен списък, ако няма високооценени ресторанти
    @Test
    void getTopRatedRestaurants_shouldReturnEmptyList_whenNoTopRatedRestaurants() throws Exception {
        when(restaurantService.getTopRatedRestaurants()).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/top-rated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    //getRestaurantsByNameAsc
    //Тества дали връща ресторантите сортирани по име във възходящ ред
    @Test
    void getRestaurantsByNameAsc_shouldReturnRestaurantsInAscendingOrder() throws Exception {
        RestaurantDto restaurant1 = new RestaurantDto();
        restaurant1.setName("Burger House");
        RestaurantDto restaurant2 = new RestaurantDto();
        restaurant2.setName("Sushi Palace");

        List<RestaurantDto> restaurants = List.of(restaurant1, restaurant2);

        when(restaurantService.getRestaurantsByNameAsc()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants/sorted/asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(restaurants.size()))
                .andExpect(jsonPath("$[0].name").value("Burger House"))
                .andExpect(jsonPath("$[1].name").value("Sushi Palace"));
    }
    //Проверява дали връща празен списък, когато няма ресторанти
    @Test
    void getRestaurantsByNameAsc_shouldReturnEmptyList_whenNoRestaurantsExist() throws Exception {
        when(restaurantService.getRestaurantsByNameAsc()).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/sorted/asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    //getRestaurantsByNameDesc
    //Тества дали връща ресторантите сортирани по име в низходящ ред
    @Test
    void getRestaurantsByNameDesc_shouldReturnRestaurantsInDescendingOrder() throws Exception {
        RestaurantDto restaurant1 = new RestaurantDto();
        restaurant1.setName("Sushi Palace");
        RestaurantDto restaurant2 = new RestaurantDto();
        restaurant2.setName("Burger House");

        List<RestaurantDto> restaurants = List.of(restaurant1, restaurant2);

        when(restaurantService.getRestaurantsByNameDesc()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants/sorted/desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(restaurants.size()))
                .andExpect(jsonPath("$[0].name").value("Sushi Palace"))
                .andExpect(jsonPath("$[1].name").value("Burger House"));
    }
    //Проверява дали връща празен списък, когато няма ресторанти
    @Test
    void getRestaurantsByNameDesc_shouldReturnEmptyList_whenNoRestaurantsExist() throws Exception {
        when(restaurantService.getRestaurantsByNameDesc()).thenReturn(List.of());

        mockMvc.perform(get("/api/restaurants/sorted/desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}
