package com.example.fooddelivery.service.restaurant;

import com.example.fooddelivery.config.restaurant.RestaurantMapper;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.repository.CuisineRepository;
import com.example.fooddelivery.repository.ProductRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final CuisineRepository cuisineRepository;
    private final UserRepository userRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 ProductRepository productRepository,
                                 CuisineRepository cuisineRepository,
                                 UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.cuisineRepository = cuisineRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant getRestaurantByName(String restaurantName) {
        return restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with this name is not found"));
    }

    @Override
    public List<Product> getAllAvailableProductsFromRestaurant(String restaurantName) {
        Restaurant restaurant = getRestaurantByName(restaurantName);

        return productRepository.findAllByRestaurantIdAndIsAvailableTrue(restaurant.getId());
    }

    @Override
    public RestaurantDto createRestaurant(RestaurantCreateDto dto, Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!("EMPLOYEE").equals(employee.getRole().getName())) {
            throw new IllegalArgumentException("Only employees can create restaurants");
        }

        Set<Cuisine> cuisines = new HashSet<>();

        for (Long cuisineId : dto.getCuisineIds()) {
            cuisines.add(cuisineRepository.findById(cuisineId)
                    .orElseThrow(() -> new EntityNotFoundException("Cuisine not found")));
        }

        Restaurant restaurant = RestaurantMapper.mapToEntity(dto, cuisines);

        return RestaurantMapper.mapToDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public void addProductToRestaurant(Long restaurantId, Long productId) {
        Restaurant restaurant = getRestaurantById(restaurantId);

        Product product = getProductById(productId);


        restaurant.addProduct(product);
        restaurantRepository.save(restaurant);

        log.info("Added product {} to restaurant {}", product.getName(), restaurant.getName());
    }

    @Transactional
    @Override
    public void removeProductFromRestaurant(Long restaurantId, Long productId) {
        Restaurant restaurant = getRestaurantById(restaurantId);

        Product product = getProductById(productId);

        if (!(product.getRestaurant().getId() == restaurantId)) {
            throw new IllegalArgumentException("Product does not belong to the given restaurant");
        }


        product.setAvailable(false);

        productRepository.save(product);

        log.info("Soft-deleted product {} from restaurant {}", product.getName(), restaurant.getName());
    }

    @Override
    public List<RestaurantDto> getRestaurantsByCuisine(Long cuisineId) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCuisineId(cuisineId);

        return restaurants.stream()
                .map(RestaurantMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> getTopRatedRestaurants(int limit) {
        return restaurantRepository.findTopByOrderByAverageRatingDesc(PageRequest.of(0, limit));
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }
}
