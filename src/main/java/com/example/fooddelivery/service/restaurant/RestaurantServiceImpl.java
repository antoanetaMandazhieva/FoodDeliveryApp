package com.example.fooddelivery.service.restaurant;

import com.example.fooddelivery.config.product.ProductMapper;
import com.example.fooddelivery.config.restaurant.RestaurantMapper;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.repository.CuisineRepository;
import com.example.fooddelivery.repository.ProductRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final CuisineRepository cuisineRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final ProductMapper productMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 ProductRepository productRepository,
                                 CuisineRepository cuisineRepository,
                                 UserRepository userRepository,
                                 RestaurantMapper restaurantMapper,
                                 ProductMapper productMapper) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.cuisineRepository = cuisineRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
        this.productMapper = productMapper;
    }

    @Override
    public RestaurantDto getRestaurantByName(String restaurantName) {
        return restaurantRepository.findByName(restaurantName).map(restaurantMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with this name is not found"));
    }

    @Override
    public List<ProductDto> getAllAvailableProductsFromRestaurant(String restaurantName) {
        RestaurantDto restaurant = getRestaurantByName(restaurantName);

        return productRepository.findAllByRestaurantIdAndIsAvailableTrue(restaurant.getId()).stream()
                .map(productMapper::mapToProductDto)
                .toList();
    }

    @Override
    public ProductDto getProductFromRestaurantByName(String restaurantName, String productName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));


        if (!restaurant.getProducts().contains(product)) {
            throw new IllegalArgumentException(String.format("Restaurant: %s doesn't contain Product: %s",
                    restaurant.getName(), product.getName()));
        }

        return productMapper.mapToProductDto(product);
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

        Restaurant restaurant = restaurantMapper.mapToEntity(dto, cuisines);

        return restaurantMapper.mapToDto(restaurantRepository.save(restaurant));
    }


    // TODO Employee should add more products not only one!
    @Transactional
    @Override
    public void addProductToRestaurant(Long employeeId, Long restaurantId, ProductDto productDto) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalStateException("Only employee can add products");
        }

        Restaurant restaurant = getRestaurantById(restaurantId);

        Product product = productMapper.mapToProduct(productDto);
        product.setAvailable(true);
        product.setRestaurant(restaurant);

        if (!product.isAvailable()) {
            product.setAvailable(true);
        }

        restaurant.addProduct(product);
        productRepository.save(product);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    @Override
    public void removeProductFromRestaurant(Long employeeId, Long restaurantId, Long productId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalStateException("Only employee can add products");
        }

        Restaurant restaurant = getRestaurantById(restaurantId);

        Product product = getProductById(productId);

        if (!(product.getRestaurant().getId() == restaurantId)) {
            throw new IllegalArgumentException("Product does not belong to the given restaurant");
        }

        product.setAvailable(false);

        productRepository.save(product);
    }

    @Override
    public List<RestaurantDto> getRestaurantsByCuisine(Long cuisineId) {
        return restaurantRepository.findAllByCuisineId(cuisineId).stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    public List<RestaurantDto> getTopRatedRestaurants() {
        return restaurantRepository.findAllByAverageRatingGreaterThanOrderByAverageRatingDesc(3.5).stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    public List<RestaurantDto> getRestaurantsByNameAsc() {
        return restaurantRepository.findAll().stream()
                .sorted(Comparator.comparing(Restaurant::getName))
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    public List<RestaurantDto> getRestaurantsByNameDesc() {
        return restaurantRepository.findAll().stream()
                .sorted(Comparator.comparing(Restaurant::getName).reversed())
                .map(restaurantMapper::mapToDto)
                .toList();
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
