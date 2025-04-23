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
import com.example.fooddelivery.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RestaurantServiceImplTest {

    private RestaurantRepository restaurantRepository;
    private ProductRepository productRepository;
    private AddressRepository addressRepository;
    private CuisineRepository cuisineRepository;
    private UserRepository userRepository;
    private RestaurantMapper restaurantMapper;
    private ProductMapper productMapper;
    private AddressMapper addressMapper;

    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        productRepository = mock(ProductRepository.class);
        addressRepository = mock(AddressRepository.class);
        cuisineRepository = mock(CuisineRepository.class);
        userRepository = mock(UserRepository.class);
        restaurantMapper = mock(RestaurantMapper.class);
        productMapper = mock(ProductMapper.class);
        addressMapper = mock(AddressMapper.class);

        restaurantService = new RestaurantServiceImpl(
                restaurantRepository, productRepository, addressRepository,
                cuisineRepository, userRepository, restaurantMapper, productMapper, addressMapper
        );
    }
    private void setId(Object target, long id) throws Exception {
        Field field = target.getClass().getSuperclass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(target, id);
    }

    @Test
    void createRestaurant_withValidEmployeeAndValidCuisines_shouldCreateRestaurantSuccessfully() throws Exception {
        // Setup employee
        User employee = new User();
        setId(employee, 10L);
        Role role = new Role();
        role.setName("EMPLOYEE");
        employee.setRole(role);

        when(userRepository.findById(10L)).thenReturn(Optional.of(employee));

        // Setup cuisine
        Cuisine cuisine1 = new Cuisine();
        setId(cuisine1, 1L);
        Cuisine cuisine2 = new Cuisine();
        setId(cuisine2, 2L);

        when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine1));
        when(cuisineRepository.findById(2L)).thenReturn(Optional.of(cuisine2));

        AddressDto addressDto = new AddressDto();
        Address address = new Address();
        setId(address, 100L);

        when(addressMapper.mapToAddress(addressDto)).thenReturn(address);

        RestaurantCreateDto dto = new RestaurantCreateDto();
        dto.setName("Test Restaurant");
        dto.setAddress(addressDto);
        dto.setCuisineIds(Set.of(1L, 2L));

        Restaurant restaurant = new Restaurant("Test Restaurant", address);
        restaurant.addCuisine(cuisine1);
        restaurant.addCuisine(cuisine2);
        setId(restaurant, 50L);

        when(restaurantMapper.mapToEntity(dto, Set.of(cuisine1, cuisine2))).thenReturn(restaurant);

        Restaurant savedRestaurant = new Restaurant();
        setId(savedRestaurant, 50L);
        when(restaurantRepository.save(restaurant)).thenReturn(savedRestaurant);

        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(50L);
        restaurantDto.setName("Test Restaurant");

        when(restaurantMapper.mapToDto(savedRestaurant)).thenReturn(restaurantDto);

        // Act
        RestaurantDto result = restaurantService.createRestaurant(dto, 10L);

        // Assert
        assertNotNull(result);
        assertEquals(50L, result.getId());
        assertEquals("Test Restaurant", result.getName());

        verify(userRepository).findById(10L);
        verify(cuisineRepository).findById(1L);
        verify(cuisineRepository).findById(2L);
        verify(addressMapper).mapToAddress(addressDto);
        verify(restaurantRepository).save(restaurant);
        verify(restaurantMapper).mapToDto(savedRestaurant);
    }
    @Test
    void createRestaurant_shouldThrowException_whenUserIsNotEmployee() throws Exception {
        // Arrange
        User user = new User();
        setId(user, 2L);//here
        user.setRole(new Role("CLIENT")); // или "SUPPLIER" или null
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        RestaurantCreateDto dto = new RestaurantCreateDto();

        IllegalArgumentException  exception = assertThrows(
                IllegalArgumentException .class,
                () -> restaurantService.createRestaurant(dto, 2L)
        );

        assertEquals("Only employees can create restaurants", exception.getMessage());
    }
    @Test
    void getRestaurantByName_shouldReturnDto_whenRestaurantExists() throws Exception{
        Restaurant restaurant = new Restaurant("Testaurant", new Address());
        setId(restaurant, 1L);//here

        when(restaurantRepository.findByName("Testaurant")).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.mapToDto(restaurant)).thenReturn(new RestaurantDto());

        RestaurantDto result = restaurantService.getRestaurantByName("Testaurant");

        assertNotNull(result);
        verify(restaurantRepository).findByName("Testaurant");
        verify(restaurantMapper).mapToDto(restaurant);
    }
    @Test
    void getRestaurantByName_shouldThrowException_whenRestaurantDoesNotExist() {
        when(restaurantRepository.findByName("Unknown")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> restaurantService.getRestaurantByName("Unknown")
        );

        assertEquals("Restaurant with this name is not found", exception.getMessage());
    }
    @Test
    void getAllAvailableProductsFromRestaurant_shouldReturnProducts_whenRestaurantExists() throws Exception{
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(1L);

        Product product = new Product();
        setId(product, 1L); //here

        ProductDto productDto = new ProductDto();

        when(restaurantRepository.findByName("Testaurant"))
                .thenReturn(Optional.of(new Restaurant("Testaurant", new Address())));
        when(restaurantMapper.mapToDto(any())).thenReturn(restaurantDto);
        when(productRepository.findAllByRestaurantIdAndIsAvailableTrue(1L)).thenReturn(List.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        List<ProductDto> result = restaurantService.getAllAvailableProductsFromRestaurant("Testaurant");

        assertEquals(1, result.size());
        assertEquals(productDto, result.get(0));
    }
    @Test
    void getAllAvailableProductsFromRestaurant_shouldThrowException_whenRestaurantNotFound() {
        when(restaurantRepository.findByName("Missing")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> restaurantService.getAllAvailableProductsFromRestaurant("Missing")
        );

        assertEquals("Restaurant with this name is not found", exception.getMessage());
    }
    @Test
    void getProductFromRestaurantByName_shouldReturnProductDto_whenFound() throws Exception{
        Restaurant restaurant = new Restaurant("Testaurant", new Address());
        setId(restaurant, 10L);//here

        Product product = new Product();
        setId(product, 20L);//here
        product.setName("Pizza");

        ProductDto productDto = new ProductDto();

        when(restaurantRepository.findByName("Testaurant")).thenReturn(Optional.of(restaurant));
        when(productRepository.findByNameAndRestaurantName("Pizza", "Testaurant")).thenReturn(Optional.of(product));
        when(productMapper.mapToProductDto(product)).thenReturn(productDto);

        ProductDto result = restaurantService.getProductFromRestaurantByName("Testaurant", "Pizza");

        assertEquals(productDto, result);
    }
    @Test
    void getProductFromRestaurantByName_shouldThrow_whenRestaurantNotFound() {
        when(restaurantRepository.findByName("Ghostaurant")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> restaurantService.getProductFromRestaurantByName("Ghostaurant", "Burger")
        );

        assertEquals("Restaurant not found", exception.getMessage());
    }
    @Test
    void getProductFromRestaurantByName_shouldThrow_whenProductNotFound() throws Exception{
        Restaurant restaurant = new Restaurant("Testaurant", new Address());
        setId(restaurant, 99L);

        when(restaurantRepository.findByName("Testaurant")).thenReturn(Optional.of(restaurant));
        when(productRepository.findByNameAndRestaurantName("Burger", "Testaurant")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> restaurantService.getProductFromRestaurantByName("Testaurant", "Burger")
        );

        assertEquals("Product: Burger not found in Restaurant: Testaurant", exception.getMessage());
    }
    @Test
    void testAddProductsToRestaurant_Success() throws Exception{
        Long employeeId = 1L;
        Long restaurantId = 2L;

        Role employeeRole = new Role();
        employeeRole.setName("EMPLOYEE");

        User employee = new User();
        setId(employee, employeeId); //here
        employee.setRole(employeeRole);

        Restaurant restaurant = new Restaurant();
        setId(restaurant, restaurantId);
        restaurant.setName("Test Restaurant");

        Cuisine cuisine = new Cuisine();
        cuisine.setName("Italian");
        Set<Cuisine> cuisines = Set.of(cuisine);

        Field cuisinesField = Restaurant.class.getDeclaredField("cuisines");
        cuisinesField.setAccessible(true);
        cuisinesField.set(restaurant, cuisines);

        ProductDto productDto = new ProductDto();
        productDto.setName("Pizza");
        productDto.setCuisineName("Italian");
        productDto.setRestaurantName("Test Restaurant");

        Product product = new Product();
        setId(product, 3L);
        product.setName("Pizza");

        List<ProductDto> productDtos = List.of(productDto);

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(productMapper.mapToProduct(productDto)).thenReturn(product);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantMapper.mapToDto(any(Restaurant.class))).thenReturn(new RestaurantDto());

        RestaurantDto result = restaurantService.addProductsToRestaurant(employeeId, restaurantId, productDtos);

        assertNotNull(result);
        verify(restaurantRepository, times(1)).save(restaurant);
    }
   /* @Test
    void addProductsToRestaurant_shouldThrowException_whenProductCuisineNotInRestaurant() throws Exception {
        Long employeeId = 1L;
        Long restaurantId = 2L;

        Role employeeRole = new Role();
        employeeRole.setName("EMPLOYEE");

        User employee = new User();
        employee.setRole(employeeRole);

        Restaurant restaurant = new Restaurant();
        restaurant.setName("PizzaPlace");

        // Set cuisines using reflection
        Cuisine cuisine = new Cuisine();
        cuisine.setName("Italian");
        Set<Cuisine> cuisines = Set.of(cuisine);
        FieldSetter.setField(restaurant, restaurant.getClass().getDeclaredField("cuisines"), cuisines);

        ProductDto productDto = new ProductDto();
        productDto.setCuisineName("Chinese");
        productDto.setRestaurantName("PizzaPlace");

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> restaurantService.addProductsToRestaurant(employeeId, restaurantId, List.of(productDto)));

        assertTrue(exception.getMessage().contains("don't have the cuisine"));
    }*/

}
