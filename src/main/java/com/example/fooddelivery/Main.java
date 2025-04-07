package com.example.fooddelivery;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.config.restaurant.RestaurantMapper;
import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.restaurant.RestaurantDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.enums.Category;
import com.example.fooddelivery.enums.Gender;
import com.example.fooddelivery.repository.*;
import com.example.fooddelivery.service.auth.AuthService;
import com.example.fooddelivery.service.restaurant.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.fooddelivery.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class Main implements CommandLineRunner {

    private AuthService authService;
    private UserService userService;
    private RestaurantService restaurantService;
    private RoleRepository roleRepository;
    private CuisineRepository cuisineRepository;
    private AddressRepository addressRepository;
    private RestaurantRepository restaurantRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public Main(AuthService authService, UserService userService, RestaurantService restaurantService, RoleRepository roleRepository, CuisineRepository cuisineRepository, AddressRepository addressRepository, RestaurantRepository restaurantRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.authService = authService;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.roleRepository = roleRepository;
        this.cuisineRepository = cuisineRepository;
        this.addressRepository = addressRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

    }
}