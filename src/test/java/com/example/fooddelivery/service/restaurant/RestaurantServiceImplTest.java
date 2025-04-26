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
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.Category;
import com.example.fooddelivery.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CuisineRepository cuisineRepository;
    @Mock private UserRepository userRepository;
    @Mock private RestaurantMapper restaurantMapper;
    @Mock private ProductMapper productMapper;
    @Mock private AddressMapper addressMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void testGetRestaurantByName_whenFound_returnsDto() {
        // Arrange
        String name = "Testaurant";
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName(name);

        when(restaurantRepository.findByName(name)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.mapToDto(restaurant)).thenReturn(restaurantDto);

        // Act
        RestaurantDto result = restaurantService.getRestaurantByName(name);

        // Assert
        assertEquals(name, result.getName());
        verify(restaurantRepository).findByName(name);
        verify(restaurantMapper).mapToDto(restaurant);
    }
    @Test
    void testGetRestaurantByName_whenNotFound_throwsException() {
        String name = "Unknown";

        when(restaurantRepository.findByName(name)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> restaurantService.getRestaurantByName(name));

        assertEquals("Restaurant with this name is not found", ex.getMessage());
    }

    @Test
    void testGetRestaurantByPartName_returnsMatchingRestaurants() {
        String partName = "Grill";
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Grill House");

        RestaurantDto dto = new RestaurantDto();
        dto.setName("Grill House");

        when(restaurantRepository.findByNameIgnoreCaseContaining(partName))
                .thenReturn(List.of(restaurant));
        when(restaurantMapper.mapToDto(restaurant)).thenReturn(dto);

        List<RestaurantDto> result = restaurantService.getRestaurantByPartName(partName);

        assertEquals(1, result.size());
        assertEquals("Grill House", result.get(0).getName());
    }
    @Test
    void testGetAllAvailableProductsFromRestaurant_returnsAvailableProducts() {
        String restaurantName = "Veggie Spot";
        long restaurantId = 99L;

        RestaurantDto dto = new RestaurantDto();
        setField(dto, "id", restaurantId); // Reflection to set ID (IdEntity pattern)
        dto.setName(restaurantName);

        Product product = new Product();
        product.setAvailable(true);
        product.setName("Salad");

        ProductDto productDto = new ProductDto();
        productDto.setName("Salad");

        when(restaurantRepository.findByName(restaurantName)).thenReturn(Optional.of(new Restaurant()));
        when(restaurantMapper.mapToDto(any())).thenReturn(dto);
        when(productRepository.findAllByRestaurantIdAndIsAvailableTrue(restaurantId)).thenReturn(List.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        List<ProductDto> result = restaurantService.getAllAvailableProductsFromRestaurant(restaurantName);

        assertEquals(1, result.size());
        assertEquals("Salad", result.get(0).getName());
    }
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllAvailableProductsFromRestaurant_whenRestaurantNotFound_throwsException() {
        when(restaurantRepository.findByName("Ghost Restaurant")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> restaurantService.getAllAvailableProductsFromRestaurant("Ghost Restaurant"));

        assertEquals("Restaurant with this name is not found", ex.getMessage());
    }
    @Test
    void getProductFromRestaurantByName_shouldReturnProductDto() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Sushi Palace");

        Product product = new Product();
        product.setName("Dragon Roll");
        product.setRestaurant(restaurant);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setName("Dragon Roll");

        when(restaurantRepository.findByName("Sushi Palace")).thenReturn(Optional.of(restaurant));
        when(productRepository.findByNameAndRestaurantName("Dragon Roll", "Sushi Palace")).thenReturn(Optional.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(expectedDto);

        ProductDto result = restaurantService.getProductFromRestaurantByName("Sushi Palace", "Dragon Roll");

        assertNotNull(result);
        assertEquals("Dragon Roll", result.getName());
    }
    @Test
    void getProductFromRestaurantByName_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findByName("Ghost Kitchen")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                restaurantService.getProductFromRestaurantByName("Ghost Kitchen", "Burger"));

        assertEquals("Restaurant not found", exception.getMessage());
    }
    @Test
    void getProductFromRestaurantByName_shouldThrowIfProductNotFound() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("BBQ House");

        when(restaurantRepository.findByName("BBQ House")).thenReturn(Optional.of(restaurant));
        when(productRepository.findByNameAndRestaurantName("Ribs", "BBQ House")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                restaurantService.getProductFromRestaurantByName("BBQ House", "Ribs"));

        assertEquals("Product: Ribs not found in Restaurant: BBQ House", exception.getMessage());
    }
    @Test
    void createRestaurant_shouldCreateSuccessfully() {
        Long employeeId = 1L;

        Role employeeRole = new Role();
        setField(employeeRole, "name", "EMPLOYEE");

        User employee = new User();
        setField(employee, "role", employeeRole);

        Cuisine cuisine = new Cuisine();
        // не сетваме ID, тъй като няма да се използва като ключ

        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Sofia");

        RestaurantCreateDto dto = new RestaurantCreateDto();
        dto.setName("Italiano");
        dto.setAddress(addressDto);
        dto.setCuisineIds(Set.of(10L));

        Address address = new Address();
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Italiano");

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setName("Italiano");

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Italiano");

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(cuisineRepository.findById(10L)).thenReturn(Optional.of(cuisine));
        when(addressMapper.mapToAddress(addressDto)).thenReturn(address);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);
        when(restaurantMapper.mapToDto(savedRestaurant)).thenReturn(restaurantDto);
        when(restaurantMapper.mapToEntity(dto, Set.of(cuisine))).thenReturn(restaurant);

        RestaurantDto result = restaurantService.createRestaurant(dto, employeeId);

        assertNotNull(result);
        assertEquals("Italiano", result.getName());
        verify(restaurantRepository).save(restaurant);
    }


    @Test
    void createRestaurant_shouldThrow_whenUserNotFound() {
        RestaurantCreateDto dto = new RestaurantCreateDto();
        dto.setCuisineIds(Set.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                restaurantService.createRestaurant(dto, 1L));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void createRestaurant_shouldThrow_whenUserNotEmployee() {
        RestaurantCreateDto dto = new RestaurantCreateDto();
        dto.setCuisineIds(Set.of(1L));

        User user = new User();
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                restaurantService.createRestaurant(dto, 1L));

        assertEquals("Only employees can create restaurants", ex.getMessage());
    }

    @Test
    void createRestaurant_shouldThrow_whenCuisineNotFound() {
        RestaurantCreateDto dto = new RestaurantCreateDto();
        dto.setCuisineIds(Set.of(99L));

        User employee = new User();
        Role role = new Role();
        role.setName("EMPLOYEE");
        employee.setRole(role);

        when(userRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(cuisineRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                restaurantService.createRestaurant(dto, 1L));

        assertEquals("Cuisine not found", ex.getMessage());
    }

    @Test
    void addProductsToRestaurant_shouldAddProductsSuccessfully() {
        User employee = new User();
        Role role = new Role();
        role.setName("EMPLOYEE");
        employee.setRole(role);

        Cuisine cuisine = new Cuisine();
        cuisine.setName("Italian");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza World");
        restaurant.addCuisine(cuisine);

        ProductDto productDto = new ProductDto();
        productDto.setName("Margherita");
        productDto.setCategory("MAIN");
        productDto.setPrice(new BigDecimal("12.99"));
        productDto.setCuisineName("Italian");
        productDto.setRestaurantName("Pizza World");

        Product product = new Product();
        product.setName("Margherita");
        product.setPrice(new BigDecimal("12.99"));
        product.setDescription("Classic pizza");
        product.setCategory(Category.MAIN);
        product.setCuisine(cuisine);
        product.setRestaurant(restaurant);

        restaurant.addProduct(product);

        // Мапнат ProductDto обратно за връщане в RestaurantDto
        ProductDto mappedProductDto = new ProductDto();
        mappedProductDto.setName("Margherita");
        mappedProductDto.setPrice(new BigDecimal("12.99"));
        mappedProductDto.setCategory("MAIN");
        mappedProductDto.setCuisineName("Italian");
        mappedProductDto.setRestaurantName("Pizza World");

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Pizza World");
        restaurantDto.setProducts(Set.of(mappedProductDto));

        when(userRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(productMapper.mapToProduct(productDto)).thenReturn(product);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantMapper.mapToDto(restaurant)).thenReturn(restaurantDto);

        RestaurantDto result = restaurantService.addProductsToRestaurant(1L, 10L, List.of(productDto));

        /* Debug
        System.out.println("Result: " + result);
        System.out.println("Restaurant name: " + result.getName());
        result.getProducts().forEach(p -> System.out.println("Product: " + p.getName() + ", price: " + p.getPrice()));*/

        assertNotNull(result);
        assertEquals("Pizza World", result.getName());
        assertEquals(1, result.getProducts().size());
        assertEquals("Margherita", result.getProducts().iterator().next().getName());

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void addProductsToRestaurant_shouldThrowIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                restaurantService.addProductsToRestaurant(1L, 10L, List.of(new ProductDto())));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void addProductsToRestaurant_shouldThrowIfUserIsNotEmployee() {
        User user = new User();
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                restaurantService.addProductsToRestaurant(1L, 10L, List.of(new ProductDto())));

        assertEquals("Only employee can add products", ex.getMessage());
    }

    @Test
    void addProductsToRestaurant_shouldThrowIfProductCuisineIsInvalid() {
        User employee = new User();
        Role role = new Role();
        role.setName("EMPLOYEE");
        employee.setRole(role);

        Cuisine cuisine = new Cuisine();
        cuisine.setName("Mexican");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Taco Place");
        restaurant.addCuisine(cuisine);

        ProductDto productDto = new ProductDto();
        productDto.setCuisineName("Italian");
        productDto.setRestaurantName("Taco Place");

        when(userRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                restaurantService.addProductsToRestaurant(1L, 10L, List.of(productDto)));

        assertTrue(ex.getMessage().contains("don't have the cuisine"));
    }

    @Test
    void addProductsToRestaurant_shouldThrowIfProductHasWrongRestaurantName() {
        User employee = new User();
        Role role = new Role();
        role.setName("EMPLOYEE");
        employee.setRole(role);

        Cuisine cuisine = new Cuisine();
        cuisine.setName("Japanese");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Sushi Bar");
        restaurant.addCuisine(cuisine);

        ProductDto productDto = new ProductDto();
        productDto.setCuisineName("Japanese");
        productDto.setRestaurantName("Wrong Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                restaurantService.addProductsToRestaurant(1L, 10L, List.of(productDto)));

        assertEquals("Wrong restaurant name", ex.getMessage());
    }
}
