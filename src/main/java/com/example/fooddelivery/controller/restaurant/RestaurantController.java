package com.example.fooddelivery.controller.restaurant;

import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.service.restaurant.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // Tested!
    @GetMapping("/name/{name}")
    public ResponseEntity<RestaurantDto> getRestaurantByName(@PathVariable String name) {
        return ResponseEntity.ok(restaurantService.getRestaurantByName(name));
    }

    // Tested!
    @GetMapping("part-name/{partName}")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByPartName(@PathVariable String partName) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByPartName(partName));
    }

    // Tested!
    @GetMapping("/{name}/products")
    public ResponseEntity<List<ProductDto>> getAllAvailableProducts(@PathVariable String name) {
        return ResponseEntity.ok(restaurantService.getAllAvailableProductsFromRestaurant(name));
    }

    // Tested!
    @GetMapping("/{restaurantName}/product/{productName}")
    public ResponseEntity<ProductDto> getProductFromRestaurant(@PathVariable String restaurantName,
                                                               @PathVariable String productName) {
        return ResponseEntity.ok(restaurantService.getProductFromRestaurantByName(restaurantName, productName));
    }

    // Tested!
    @PostMapping("/create/{employeeId}")
    public ResponseEntity<RestaurantDto> createRestaurant(@PathVariable Long employeeId,
                                                          @RequestBody RestaurantCreateDto dto) {
        return ResponseEntity.ok(restaurantService.createRestaurant(dto, employeeId));
    }

    // Tested!
    @PostMapping("/{restaurantId}/products/add")
    public ResponseEntity<RestaurantDto> addProductsToRestaurant(@PathVariable Long restaurantId,
                                                                 @RequestParam Long employeeId,
                                                                 @RequestBody List<ProductDto> productDtos) {
        return ResponseEntity.ok(restaurantService.addProductsToRestaurant(employeeId, restaurantId, productDtos));
    }

    // Tested!
    @PutMapping("/{restaurantId}/remove-product/{productId}/by/{employeeId}")
    public ResponseEntity<RestaurantDto> removeProductFromRestaurant(@PathVariable Long restaurantId,
                                                                     @PathVariable Long productId,
                                                                     @PathVariable Long employeeId) {
        return ResponseEntity.ok(restaurantService.removeProductFromRestaurant(employeeId, restaurantId, productId));
    }

    // Tested!
    @GetMapping("/cuisine/{cuisineId}")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByCuisine(@PathVariable Long cuisineId) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCuisine(cuisineId));
    }

    /**
     * @return All restaurants with average rating > 3.5
     */

    // Tested!
    @GetMapping("/top-rated")
    public List<RestaurantDto> getTopRatedRestaurants() {
        return restaurantService.getTopRatedRestaurants();
    }

    // Tested!
    @GetMapping("/sorted/asc")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByNameAsc() {
        return ResponseEntity.ok(restaurantService.getRestaurantsByNameAsc());
    }

    // Tested!
    @GetMapping("/sorted/desc")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsByNameDesc() {
        return ResponseEntity.ok(restaurantService.getRestaurantsByNameDesc());
    }

}