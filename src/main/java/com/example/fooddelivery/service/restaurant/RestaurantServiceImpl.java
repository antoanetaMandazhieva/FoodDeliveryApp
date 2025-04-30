package com.example.fooddelivery.service.restaurant;

import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.restaurant.RestaurantCreateDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.cuisine.Cuisine;
import com.example.fooddelivery.entity.product.Product;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.exception.restaurant.ProductNotInRestaurantException;
import com.example.fooddelivery.exception.restaurant.RestaurantNotHaveCuisineException;
import com.example.fooddelivery.exception.restaurant.WrongRestaurantNameException;
import com.example.fooddelivery.exception.role.InvalidRoleException;
import com.example.fooddelivery.mapper.address.AddressMapper;
import com.example.fooddelivery.mapper.product.ProductMapper;
import com.example.fooddelivery.mapper.restaurant.RestaurantMapper;
import com.example.fooddelivery.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.fooddelivery.util.SystemErrors.Product.*;
import static com.example.fooddelivery.util.SystemErrors.Restaurant.*;
import static com.example.fooddelivery.util.SystemErrors.User.USER_NOT_FOUND;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final String EMPLOYEE_ROLE = "EMPLOYEE";

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
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_WITH_THIS_NAME_NOT_FOUND));
    }

    // Tested!
    @Override
    @Transactional
    public List<RestaurantDto> getRestaurantsByPartName(String partName) {
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
        validateRestaurantExists(restaurantName);

        Product product = getProductByNameAndRestaurantName(productName, restaurantName);


        return productMapper.mapToProductDto(product);
    }

    @Override
    @Transactional
    public RestaurantDto createRestaurant(RestaurantCreateDto dto, Long employeeId) {
        User employee = getUser(employeeId);
        validateIsEmployee(employee, ONLY_EMPLOYEE_CREATE_RESTAURANT);

        Set<Cuisine> cuisines = getCuisinesFromDto(dto);

        Restaurant restaurant = restaurantMapper.mapToEntity(dto, cuisines);

        Address address = addressMapper.mapToAddress(dto.getAddress());
        address.addRestaurant(restaurant);

        return restaurantMapper.mapToDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public RestaurantDto addProductsToRestaurant(Long employeeId, Long restaurantId, List<ProductDto> productDtos) {
        User employee = getUser(employeeId);
        validateIsEmployee(employee, ONLY_EMPLOYEE_ADD_PRODUCTS_TO_RESTAURANT);

        Restaurant restaurant = getRestaurantById(restaurantId);

        Set<String> cuisineNames = restaurant.getCuisines().stream()
                .map(Cuisine::getName)
                .collect(Collectors.toSet());

        validateCuisines(restaurant, productDtos, cuisineNames);

        addOrUpdateProducts(restaurant, productDtos);

        return restaurantMapper.mapToDto(restaurant);
    }

    @Transactional
    @Override
    public RestaurantDto removeProductFromRestaurant(Long employeeId, Long restaurantId, Long productId) {
        User employee = getUser(employeeId);
        validateIsEmployee(employee, ONLY_EMPLOYEE_REMOVE_PRODUCT_FROM_RESTAURANT);

        Restaurant restaurant = getRestaurantById(restaurantId);
        Product product = getProductById(productId);

        validateIsProductInRestaurant(product, restaurantId);
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

    private void validateRestaurantExists(String restaurantName) {
        restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private void validateIsEmployee(User employee, String message) {
        if (!EMPLOYEE_ROLE.equals(employee.getRole().getName())) {
            throw new InvalidRoleException(message);
        }
    }

    private Set<Cuisine> getCuisinesFromDto(RestaurantCreateDto dto) {
        Set<Cuisine> cuisines = new HashSet<>();

        for (Long cuisineId : dto.getCuisineIds()) {
            if (isCuisineExists(cuisineId)) {
                Cuisine cuisine = getCuisine(cuisineId);

                cuisines.add(cuisine);
            }
        }

        return cuisines;
    }

    private boolean isCuisineExists(Long cuisineId) {
        return cuisineRepository.findById(cuisineId).isPresent();
    }

    private Cuisine getCuisine(Long cuisineId) {
        return cuisineRepository.findById(cuisineId).get();
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException(RESTAURANT_NOT_FOUND));
    }

    private void validateCuisines(Restaurant restaurant, List<ProductDto> productDtos, Set<String> cuisineNames) {
        for (ProductDto productDto : productDtos) {
            validateRestaurantHaveThisCuisine(restaurant, cuisineNames, productDto);

            validateRestaurantNameEqualToProductRestaurantName(restaurant, productDto);
        }
    }

    private void validateRestaurantHaveThisCuisine(Restaurant restaurant, Set<String> cuisineNames, ProductDto productDto) {
        if (!cuisineNames.contains(productDto.getCuisineName())) {
            throw new RestaurantNotHaveCuisineException(String.format(RESTAURANT_NOT_HAVE_CUISINE,
                    restaurant.getName(), productDto.getCuisineName(), String.join(", ", cuisineNames)));
        }
    }

    private void validateRestaurantNameEqualToProductRestaurantName(Restaurant restaurant, ProductDto productDto) {
        if (!restaurant.getName().equals(productDto.getRestaurantName())) {
            throw new WrongRestaurantNameException(WRONG_RESTAURANT_NAME);
        }
    }

    private void addOrUpdateProducts(Restaurant restaurant, List<ProductDto> productDtos) {
        // Взимат се имената на всички продукти, които ще добавяме
        List<String> productNames = productDtos.stream()
                .map(ProductDto::getName)
                .toList();

        // Взимат се всички съществуващи продукти за този ресторант по име
        List<Product> existingProducts = productRepository.findAllByNameInAndRestaurantId(productNames, restaurant.getId());

        // Мапване на продуктите по име за бързо търсене
        Map<String, Product> existingProductMap = existingProducts.stream()
                .collect(Collectors.toMap(Product::getName, p -> p));

        List<Product> productsToSave = new ArrayList<>();

        for (ProductDto productDto : productDtos) {
            Product existingProduct = existingProductMap.get(productDto.getName());

            if (existingProduct != null) {
                existingProduct.setAvailable(true);
                productsToSave.add(existingProduct);
            } else {
                Product newProduct = productMapper.mapToProduct(productDto);
                newProduct.setAvailable(true);
                newProduct.setRestaurant(restaurant);
                productsToSave.add(newProduct);
                restaurant.addProduct(newProduct);
            }
        }

        // Съхраняване на всичко
        productRepository.saveAll(productsToSave);

        // Накрая се запазва ресторанта, ако има нови продукти добавени
        restaurantRepository.save(restaurant);
    }

    private Product getProductByNameAndRestaurantName(String productName, String restaurantName) {
        return productRepository.findByNameAndRestaurantName(productName, restaurantName)
                .orElseThrow(() -> new ProductNotInRestaurantException(PRODUCT_IN_THIS_RESTAURANT_NOT_FOUND));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
    }

    private void validateIsProductInRestaurant(Product product, Long restaurantId) {
        if (!(product.getRestaurant().getId() == restaurantId)) {
            throw new ProductNotInRestaurantException(PRODUCT_IN_THIS_RESTAURANT_NOT_FOUND);
        }
    }
}