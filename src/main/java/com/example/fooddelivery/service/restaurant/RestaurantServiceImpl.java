package com.example.fooddelivery.service.restaurant;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.product.ProductMapper;
import com.example.fooddelivery.config.restaurant.RestaurantMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.cuisine.Cuisine;
import com.example.fooddelivery.entity.product.Product;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final CuisineRepository cuisineRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final ProductMapper productMapper;
    private final AddressMapper addressMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 ProductRepository productRepository,
                                 CuisineRepository cuisineRepository,
                                 UserRepository userRepository,
                                 RestaurantMapper restaurantMapper,
                                 ProductMapper productMapper,
                                 AddressMapper addressMapper) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.cuisineRepository = cuisineRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
        this.productMapper = productMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    @Transactional
    public RestaurantDto getRestaurantByName(String restaurantName) {
        return restaurantRepository.findByName(restaurantName)
                .map(restaurantMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with this name is not found"));
    }

    // Tested!
    @Override
    public List<RestaurantDto> getRestaurantByPartName(String partName) {
        return restaurantRepository.findByNameIgnoreCaseContaining(partName).stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public List<ProductDto> getAllAvailableProductsFromRestaurant(String restaurantName) {
        RestaurantDto restaurant = getRestaurantByName(restaurantName);

        return productRepository.findAllByRestaurantIdAndIsAvailableTrue(restaurant.getId()).stream()
                .map(productMapper::mapToProductDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductDto getProductFromRestaurantByName(String restaurantName, String productName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));


        Product product = productRepository.findByNameAndRestaurantName(productName, restaurantName)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Product: %s not found in Restaurant: %s", productName, restaurantName
                )));


        return productMapper.mapToProductDto(product);
    }

    @Override
    @Transactional
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

        Address address = addressMapper.mapToAddress(dto.getAddress());
        address.addRestaurant(restaurant);

        return restaurantMapper.mapToDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public RestaurantDto addProductsToRestaurant(Long employeeId, Long restaurantId, List<ProductDto> productDtos) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalStateException("Only employee can add products");
        }

        Restaurant restaurant = getRestaurantById(restaurantId);

        Set<String> cuisineNames = restaurant.getCuisines().stream()
                .map(Cuisine::getName)
                .collect(Collectors.toSet());

        for (ProductDto productDto : productDtos) {
            if (!cuisineNames.contains(productDto.getCuisineName())) {
                throw new IllegalArgumentException(String.format(
                        "Restaurant: %s don't have the cuisine (%s) of the product you want to add. Restaurant cuisines: %s" ,
                        restaurant.getName(), productDto.getCuisineName(), String.join(", ", cuisineNames)));
            }

            if (!restaurant.getName().equals(productDto.getRestaurantName())) {
                throw new IllegalArgumentException("Wrong restaurant name");
            }
        }


        List<Product> products = productDtos.stream()
                .map(productMapper::mapToProduct)
                .toList();

        products.forEach(product -> product.setAvailable(true));

        for (Product product : products) {
            restaurant.addProduct(product);
            restaurantRepository.save(restaurant);
        }

        return restaurantMapper.mapToDto(restaurant);
    }

    @Transactional
    @Override
    public RestaurantDto removeProductFromRestaurant(Long employeeId, Long restaurantId, Long productId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"EMPLOYEE".equals(employee.getRole().getName())) {
            throw new IllegalStateException("Only employee can remove products");
        }

        Restaurant restaurant = getRestaurantById(restaurantId);

        Product product = getProductById(productId);

        if (!(product.getRestaurant().getId() == restaurantId)) {
            throw new IllegalArgumentException("Product does not belong to the given restaurant");
        }

        product.setAvailable(false);
        productRepository.save(product);

        return restaurantMapper.mapToDto(restaurant);
    }

    @Override
    @Transactional
    public List<RestaurantDto> getRestaurantsByCuisine(Long cuisineId) {
        return restaurantRepository.findAllByCuisineId(cuisineId).stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public List<RestaurantDto> getTopRatedRestaurants() {
        return restaurantRepository.findAllByAverageRatingGreaterThanOrderByAverageRatingDesc(3.5).stream()
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public List<RestaurantDto> getRestaurantsByNameAsc() {
        return restaurantRepository.findAll().stream()
                .sorted(Comparator.comparing(Restaurant::getName))
                .map(restaurantMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
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
