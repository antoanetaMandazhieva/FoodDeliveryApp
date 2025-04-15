package com.example.fooddelivery;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.entity.Address;
import com.example.fooddelivery.entity.Cuisine;
import com.example.fooddelivery.entity.Product;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.enums.Category;
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
import java.util.List;
import java.util.Optional;

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

//        Cuisine italian = new Cuisine("Italian");
//        Cuisine chinese = new Cuisine("Chinese");
//        Cuisine indian = new Cuisine("Indian");
//        Cuisine japanese = new Cuisine("Japanese");
//        Cuisine mexican = new Cuisine("Mexican");
//        Cuisine french = new Cuisine("French");
//        Cuisine thai = new Cuisine("Thai");
//        Cuisine mediterranean = new Cuisine("Mediterranean");
//        Cuisine korean = new Cuisine("Korean");
//        Cuisine lebanese = new Cuisine("Lebanese");
//        Cuisine americanFastFood = new Cuisine("American Fast Food");
//        Cuisine seafood = new Cuisine("Seafood");
//        Cuisine bulgarian = new Cuisine("Bulgarian");
//        Cuisine vietnamese = new Cuisine("Vietnamese");
//
//        List<Cuisine> cuisines = List.of(italian, chinese, indian, japanese, mexican, french,
//        thai, mediterranean, korean, lebanese, americanFastFood, seafood, bulgarian, vietnamese);
//
//        cuisineRepository.saveAll(cuisines);


//        Address laDolceVitaAddress = new Address("Buzludzha street", "Sofia Center", "1606", "Bulgaria");
//        addressRepository.save(laDolceVitaAddress);

//        Restaurant laDolceVita = restaurantRepository.findById(1L).get();
//        laDolceVita.addCuisine(cuisineRepository.findByName("Italian").get());
//        laDolceVita.addAddress(addressRepository.findById(1L).get());
//
//        restaurantRepository.save(laDolceVita);


//        Restaurant laDolceVita = restaurantRepository.findById(1L).get();
//        laDolceVita.addCuisine(cuisineRepository.findByName("Italian").get());
//
//        restaurantRepository.save(laDolceVita);


//        Address dragonsFeastAddress = new Address("Zona B5 - Bl.2", "Sofia", "1303", "Bulgaria");
//        addressRepository.save(dragonsFeastAddress);
//        Restaurant dragonsFeast = new Restaurant("Dragon's Feast", addressRepository.findById(2L).get());
//        dragonsFeast.addCuisine(cuisineRepository.findByName("Chinese").get());
//        restaurantRepository.save(dragonsFeast);

//        Restaurant dragonsFeast = restaurantRepository.findByName("Dragon's Feast").get();
//        dragonsFeast.addCuisine(cuisineRepository.findByName("Chinese").get());
//
//        restaurantRepository.save(dragonsFeast);

//        Address spiceSymphonyAddress = new Address("Buzludzha street", "Sofia Center", "1606", "Bulgaria");
//        addressRepository.save(spiceSymphonyAddress);

//        Restaurant spiceSymphony = new Restaurant("Spice Symphony", addressRepository.findById(3L).get());
//        spiceSymphony.addCuisine(cuisineRepository.findByName("Indian").get());
//
//        restaurantRepository.save(spiceSymphony);

//        Address sakuraHavenAddress = new Address("Sofiyski Geroy 1 Street", "Sofia Hipodruma", "1612", "Bulgaria");
//        addressRepository.save(sakuraHavenAddress);
//
//        Restaurant sakuraHaven = new Restaurant("Sakura Haven", addressRepository.findById(4L).get());
//        sakuraHaven.addCuisine(cuisineRepository.findByName("Japanese").get());
//
//        restaurantRepository.save(sakuraHaven);


//        Address elSabrosoAddress = new Address("Evlogi & Hristo Georgiev Blvd.", "Sofia", "1164", "Bulgaria");
//        addressRepository.save(elSabrosoAddress);
//
//        Restaurant elSabroso = new Restaurant("El Sabroso", addressRepository.findById(5L).get());
//        elSabroso.addCuisine(cuisineRepository.findByName("Mexican").get());
//
//        restaurantRepository.save(elSabroso);

//        Address leGourmetAddress = new Address("Vasil Levski 82 Blvd.", "Sofia Center", "1142", "Bulgaria");
//        addressRepository.save(leGourmetAddress);
//
//        Restaurant leGourmet = new Restaurant("Le Gourmet", addressRepository.findById(6L).get());
//        leGourmet.addCuisine(cuisineRepository.findByName("French").get());
//
//        restaurantRepository.save(leGourmet);
//


//        Address siamSpiceAddress = new Address("Lozenska Planina 10 Street", "Sofia g.k. Lozenets", "1421", "Bulgaria");
//        addressRepository.save(siamSpiceAddress);
//
//        Restaurant siamSpice = new Restaurant("Siam Spice", addressRepository.findById(7L).get());
//        siamSpice.addCuisine(cuisineRepository.findByName("Thai").get());
//
//        restaurantRepository.save(siamSpice);

//        Address oliveOreganoAddress = new Address("Mihai Eminescu 1 Blvd.", "Sofia Oborishte", "1124", "Bulgaria");
//        addressRepository.save(oliveOreganoAddress);

//        Restaurant oliveOregano = new Restaurant("Olive & Oregano", addressRepository.findById(8L).get());
//        oliveOregano.addCuisine(cuisineRepository.findByName("Thai").get());
//        Restaurant oliveOregano = restaurantRepository.findByName("Olive & Oregano").get();
//        oliveOregano.addCuisine(cuisineRepository.findByName("Mediterranean").get());
//
//        restaurantRepository.save(oliveOregano);

//        Address address = new Address("Buxton Brothers 34 Blvd.", "Sofia", "1618", "Bulgaria");
//        addressRepository.save(address);

//        Restaurant restaurant = new Restaurant("Seoul Sensation", addressRepository.findById(9L).get());
//        restaurant.addCuisine(cuisineRepository.findByName("Korean").get());
//
//        restaurantRepository.save(restaurant);

//    Address address = new Address("Nishava 111 Street", "Sofia g.k. Strelbishte", "1408", "Bulgaria");
//    addressRepository.save(address);

//    Restaurant restaurant = new Restaurant("Cedars of Lebanon", addressRepository.findById(10L).get());
//    restaurant.addCuisine(cuisineRepository.findByName("Lebanese").get());
//
//    restaurantRepository.save(restaurant);

//        Address address = new Address("Kliment Ohridski 10 Blvd.", "Sofia g.k. Darvenitsa", "1756", "Bulgaria");
//        addressRepository.save(address);

//        Restaurant restaurant = new Restaurant("Burger Boulevard", addressRepository.findById(11L).get());
//        restaurant.addCuisine(cuisineRepository.findByName("American Fast Food").get());
//
//        restaurantRepository.save(restaurant);


//        Address address1 = new Address("Iordan Radichkov Street", "Sofia g.k. Vitosha", "1700", "Bulgaria");
//        Address address2 = new Address("Krastyu Pastuhov 34 Blvd.", "Sofia g.k. Drujba", "1592", "Bulgaria");
//        Address address3 = new Address("Tsaritsa Yoanna 23 Blvd.", "Sofia g.k. Lyulin", "1324", "Bulgaria");
//
//        addressRepository.saveAll(List.of(address1, address2, address3));

//        Restaurant restaurant1 = new Restaurant("Ocean's Bounty", addressRepository.findById(12L).get());
//        restaurant1.addCuisine(cuisineRepository.findByName("Seafood").get());
//
//        Restaurant restaurant2 = new Restaurant("Balkan Bistro", addressRepository.findById(14L).get());
//        restaurant2.addCuisine(cuisineRepository.findByName("Bulgarian").get());
//
//        Restaurant restaurant3 = new Restaurant("Saigon Flavors", addressRepository.findById(13L).get());
//        restaurant3.addCuisine(cuisineRepository.findByName("Vietnamese").get());
//
//        restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));


//
//        addAddress("Dondukov 91 Blvd.", "Sofia Center", "1000", "Bulgaria", addressRepository);
//        addAddress("Montevideo Street", "Sofia Ovcha Kupel", "1000", "Bulgaria", addressRepository);
//        addAddress("Pirin 12A Street", "Sofia Borovo", "1000", "Bulgaria", addressRepository);
//        addAddress("Alabin Street", "Sofia Oborishte", "1000", "Bulgaria", addressRepository);
//        addAddress("Aleksandar Malinov 69 Blvd.", "Sofia Mladost", "1000", "Bulgaria", addressRepository);
//        addAddress("Vladimir Vazov Blvd.", "Sofia Levski", "1000", "Bulgaria", addressRepository);




//        Cuisine italian = cuisineRepository.findByName("Italian").get();
//        Cuisine mediterranean = cuisineRepository.findByName("Mediterranean").get();
//        Cuisine greek = cuisineRepository.findByName("Greek").get();
//
//        Cuisine indian = cuisineRepository.findByName("Indian").get();
//        Cuisine thai = cuisineRepository.findByName("Thai").get();
//        Cuisine chinese = cuisineRepository.findByName("Chinese").get();
//
//        Cuisine mexican = cuisineRepository.findByName("Mexican").get();
//        Cuisine americanFastFood = cuisineRepository.findByName("American Fast Food").get();
//        Cuisine spanish = cuisineRepository.findByName("Spanish").get();
//
//
//        Cuisine japanese = cuisineRepository.findByName("Japanese").get();
//        Cuisine korean = cuisineRepository.findByName("Korean").get();
//        Cuisine vietnamese = cuisineRepository.findByName("Vietnamese").get();
//
//
//        Cuisine french = cuisineRepository.findByName("French").get();
//        Cuisine german = cuisineRepository.findByName("German").get();
//        Cuisine british = cuisineRepository.findByName("British").get();
//
//        Cuisine lebanese = cuisineRepository.findByName("Lebanese").get();
//        Cuisine turkish = cuisineRepository.findByName("Turkish").get();
//        Cuisine moroccan = cuisineRepository.findByName("Moroccan").get();


//
//        addRestaurant("Global Bites", addressRepository.findById(15L).get(), List.of(italian, mediterranean, greek), restaurantRepository);
//        addRestaurant("World Flavors", addressRepository.findById(16L).get(), List.of(indian, thai, chinese), restaurantRepository);
//        addRestaurant("Fusion Fiesta", addressRepository.findById(17L).get(), List.of(americanFastFood, mexican, spanish), restaurantRepository);
//        addRestaurant("Culinary Carnival", addressRepository.findById(18L).get(), List.of(japanese, korean, vietnamese), restaurantRepository);
//        addRestaurant("Epicurean Junction", addressRepository.findById(19L).get(), List.of(french, german, british), restaurantRepository);
//        addRestaurant("Taste of the World", addressRepository.findById(20L).get(), List.of(lebanese, turkish, moroccan), restaurantRepository);

//        Cuisine turkish = new Cuisine("Turkish");
//        Cuisine moroccan = new Cuisine("Moroccan");
//
//        cuisineRepository.saveAll(List.of(turkish, moroccan));

//        Restaurant restaurant = restaurantRepository.findById(20L).get();
//
//        restaurant.addCuisine(lebanese);
//        restaurant.addCuisine(turkish);
//        restaurant.addCuisine(moroccan);
//
//        restaurantRepository.save(restaurant);


//        Product spaghettiBolognese = new Product("Spaghetti Bolognese", BigDecimal.valueOf(14.50),
//                "Classic pasta served in a rich meat sauce with tomatoes, garlic, and basil.",
//                Category.MAIN);

//        Restaurant restaurant = restaurantRepository.findById(16L).get();
//        Cuisine chinese = cuisineRepository.findByName("Chinese").get();
//        Cuisine indian = cuisineRepository.findByName("Indian").get();
//
//        Product product = createProduct("Mango Pudding", BigDecimal.valueOf(12.50),
//                    "Smooth, fruity pudding with a burst of fresh mango flavor.",
//                Category.DESSERTS, chinese, restaurant);


    }

    private static void addAddress(String street, String city, String postalCode, String country, AddressRepository repository) {
        repository.save(new Address(street, city, postalCode, country));
    }

    private static void addRestaurant(String name, Address address, List<Cuisine> cuisines, RestaurantRepository repository) {
        Restaurant restaurant = new Restaurant(name, address);
        for (Cuisine cuisine: cuisines) {
            restaurant.addCuisine(cuisine);
        }

        repository.save(restaurant);
    }

    private Product createProduct(String name, BigDecimal value, String description,
                                  Category category, Cuisine cuisine, Restaurant restaurant) {
        return new Product(name, value, description, category, cuisine, restaurant);
    }
}