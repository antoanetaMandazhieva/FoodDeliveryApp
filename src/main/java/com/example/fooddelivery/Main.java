package com.example.fooddelivery;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.enums.Category;
import com.example.fooddelivery.enums.Gender;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.repository.*;
import com.example.fooddelivery.service.auth.AuthService;
import com.example.fooddelivery.service.order.OrderService;
import com.example.fooddelivery.service.restaurant.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.fooddelivery.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class Main implements CommandLineRunner {

    private final OrderService orderService;
    private final AuthService authService;
    private UserService userService;
    private RestaurantService restaurantService;
    private RoleRepository roleRepository;
    private CuisineRepository cuisineRepository;
    private AddressRepository addressRepository;
    private RestaurantRepository restaurantRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private AddressMapper addressMapper;

    public Main(OrderService orderService, AuthService authService, UserService userService, RestaurantService restaurantService, RoleRepository roleRepository, CuisineRepository cuisineRepository, AddressRepository addressRepository, RestaurantRepository restaurantRepository, ProductRepository productRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.orderService = orderService;
        this.authService = authService;
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.roleRepository = roleRepository;
        this.cuisineRepository = cuisineRepository;
        this.addressRepository = addressRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws AccessDeniedException {

//        User admin = new User("ivan123@example.com", "9876543210", "ivan123", "Ivan", "Ivanov",
//                Gender.MALE, LocalDate.of(1980, 9, 8), "0888987553");
//
//        AddressDto addressDto = new AddressDto("Rakovska", "Sofia", "1000", "Bulgaria");
//        Address address = addressMapper.mapToAddress(addressDto);
//
//        admin.addAddress(address);
//        admin.setRole(roleRepository.findByName("ADMIN").get());
//
//        userRepository.save(admin);


//        authService.register(new RegisterRequestDto("abcd@example.com", "1234567890", "client1", "First", "Client",
//                Gender.MALE, LocalDate.of(2000, 11, 5), "0888999999",
//                new AddressDto("Dondukov", "Sofia", "1000", "Bulgaria")));
//
//        authService.register(new RegisterRequestDto("milenaS@example.com", "5555555555", "milenaStv", "Milena", "Stamova",
//                Gender.FEMALE, LocalDate.of(1995, 5, 26), "0888923450",
//                new AddressDto("Krasna Polyana - 700", "Sofia", "1000", "Bulgaria")));
//
//        authService.register(new RegisterRequestDto("stoyanZ@example.com", "2214577248", "stoyan11", "Stoyan", "Zamfirov",
//                Gender.MALE, LocalDate.of(2005, 8, 20), "0888754231",
//                new AddressDto("Lyulin - ul. 715", "Sofia", "1000", "Bulgaria")));
//
//        authService.register(new RegisterRequestDto("miho@example.com", "1234444444", "Mihan69", "Miho", "Mihov",
//                Gender.MALE, LocalDate.of(2008, 7, 3), "0888999426",
//                new AddressDto("Oborishte 21", "Sofia", "1000", "Bulgaria")));
//
//        authService.register(new RegisterRequestDto("princess6969@example.com", "0888696996", "kitty_queen", "Little", "Princess",
//                Gender.FEMALE, LocalDate.of(2010, 3, 5), "0888999908",
//                new AddressDto("Aleksandar Malinov 20", "Sofia", "1000", "Bulgaria")));
//
//        authService.register(new RegisterRequestDto("krisCR7@example.com", "1234554321", "CR7_1106", "Kristian", "Georgiev",
//                Gender.MALE, LocalDate.of(2003, 9, 14), "0888990651",
//                new AddressDto("Drujba 123", "Sofia", "1000", "Bulgaria")));
//
//        authService.register(new RegisterRequestDto("hrisipissi@example.com", "ASDF12345", "Hristina1234", "Hristina", "Dimitrova",
//                Gender.FEMALE, LocalDate.of(2001, 2, 20), "0888010123",
//                new AddressDto("St. grad 1000", "Sofia", "1000", "Bulgaria")));

//        Address address = addressRepository.findById(8L).get();
//        AddressDto dto = addressMapper.toDto(address);
//
//
//        userService.updateUser(8L, new UserProfileDto("hrisipissi@example.com", "Hristina1234", "Hristina", "Dimitrova",
//                Gender.FEMALE, LocalDate.of(2001, 2, 20), "0888010123",
//             Set.of(dto, new AddressDto("Mladost 4", "Sofia", "1000", "Bulgaria"))));


//        Cuisine asian = new Cuisine("Asian");
//        Cuisine italian = new Cuisine("Italian");
//        Cuisine bulgarian = new Cuisine("Bulgarian");
//        Cuisine french = new Cuisine("French");
//        Cuisine fastFood = new Cuisine("Fast Food");
//
//        cuisineRepository.save(asian);
//        cuisineRepository.save(italian);
//        cuisineRepository.save(bulgarian);
//        cuisineRepository.save(french);
//        cuisineRepository.save(fastFood);


//        Cuisine asian = cuisineRepository.findById(1L).get();
//        Cuisine italian = cuisineRepository.findById(2L).get();
//        Cuisine bulgarian = cuisineRepository.findById(3L).get();
//        Cuisine french = cuisineRepository.findById(4L).get();
//        Cuisine fastFood = cuisineRepository.findById(5L).get();
//
//
//        Address address = new Address("Rakovska 123", "Sofia", "1000", "Bulgaria");
//        addressRepository.save(address);
//
//
//        Restaurant happy1 = new Restaurant("Happy 1", address);
//
//        happy1.addCuisine(asian);
//        happy1.addCuisine(italian);
//        happy1.addCuisine(bulgarian);
//        happy1.addCuisine(french);
//        happy1.addCuisine(fastFood);
//
//
//        restaurantRepository.save(happy1);

//        Restaurant restaurant = restaurantRepository.findById(1L).get();
//
//        restaurant.addProduct(new Product("Caesar", BigDecimal.valueOf(12.5),
//                "Very tasty salad with chicken", Category.SALADS));

//        restaurant.addProduct(new Product("Coke", BigDecimal.valueOf(3.5),
//                "Very tasty drink", Category.DRINKS));

//        Product beefBurger = new Product("Beef Burger", BigDecimal.valueOf(12.5),
//                "Very tasty burger with beef", Category.BURGERS);
//
//
//
//        restaurant.addProduct(beefBurger);

//        restaurant.addProduct(new Product("White wine", BigDecimal.valueOf(35),
//                "French white wine", Category.ALCOHOLS));

//        restaurant.addProduct(new Product("Chicken Soup", BigDecimal.valueOf(10),
//                "Very tasty chicken soup", Category.SOUPS));

//        restaurant.addProduct(new Product("Ice-cream", BigDecimal.valueOf(8.3),
//                "Very tasty ice-cream", Category.DESSERTS));


//        restaurantService.removeProductFromRestaurant(1L, 7L);

//        List<ProductDto> allAvailableProductsFromRestaurant =
//                restaurantService.getAllAvailableProductsFromRestaurant("Happy 1");
//
//
//        for (ProductDto product : allAvailableProductsFromRestaurant) {
//            System.out.println(product.getName());
//        }


//     orderService.createOrder(new OrderCreateDto(1L, Set.of(1L, 2L, 4L)), 3L);

//        orderService.cancelOrderByClient(1L, 3L);

//        orderService.createOrder(new OrderCreateDto(1L, Set.of(1L, 2L, 5L)), 3L);

//        orderService.updateOrderStatus(2L, OrderStatus.ACCEPTED, 2L);

//        orderService.assignOrderToSupplier(2L, 4L);
//        OrderDto orderDto = orderService.finishOrder(2L, 4L);

//        orderService.createOrder(new OrderCreateDto(1L, Set.of(6L, 7L)), 1L);

//        orderService.assignOrderToSupplier(3L, 4L);

//        orderService.updateOrderStatus(4L, 2L);
//        orderService.updateOrderStatus(4L, 2L);

//        orderService.finishOrder(3L, 4L);
//        orderService.assignOrderToSupplier(4L, 4L);
//
//        orderService.takeOrder(4L, 4L);
//        orderService.finishOrder(4L, 4L);

//        Role adminRole = new Role("ADMIN");
//        Role clientRole = new Role("CLIENT");
//        Role supplierRole = new Role("SUPPLIER");
//        Role employeeRole = new Role("EMPLOYEE");
//
//        roleRepository.saveAll(List.of(adminRole, clientRole, supplierRole, employeeRole));
    }
}