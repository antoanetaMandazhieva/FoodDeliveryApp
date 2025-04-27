package com.example.fooddelivery.mappers.OrderMapper;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.config.ordered_item.OrderedItemMapper;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderProductDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.order.Order;
import com.example.fooddelivery.entity.ordered_item.OrderedItem;
import com.example.fooddelivery.entity.ordered_item.OrderedItemId;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.dto.address.AddressDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderMapperTest {

    @Mock private ModelMapper modelMapper;
    @Mock private AddressMapper addressMapper;
    @Mock private OrderedItemMapper orderedItemMapper;

    @InjectMocks
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //mapFromOrderToDto
    //Проверява дали успешно мапва Order към OrderDto без отстъпка
    @Test
    void mapFromOrderToDto_shouldMapCorrectly_whenNoDiscount() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        User client = new User();
        client.setName("Ivan");
        order.setClient(client);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza Place");
        order.setRestaurant(restaurant);

        OrderedItem orderedItem = mock(OrderedItem.class);
        OrderedItemId itemId = mock(OrderedItemId.class);
        when(itemId.getProductId()).thenReturn(1L);
        when(orderedItem.getId()).thenReturn(itemId);
        order.setOrderedItems(Set.of(orderedItem));

        OrderDto mappedDto = new OrderDto();
        mappedDto.setClientName("Ivan");
        mappedDto.setRestaurantName("Pizza Place");
        mappedDto.setOrderStatus(OrderStatus.PENDING.name());
        mappedDto.setProducts(new ArrayList<>());

        when(modelMapper.map(order, OrderDto.class)).thenReturn(mappedDto);

        OrderProductDto productDto = new OrderProductDto();
        when(orderedItemMapper.mapToProductDto(orderedItem)).thenReturn(productDto);

        OrderDto result = orderMapper.mapFromOrderToDto(order);

        assertEquals("Ivan", result.getClientName());
        assertEquals("Pizza Place", result.getRestaurantName());
        assertEquals(OrderStatus.PENDING.name(), result.getOrderStatus());
        assertEquals(1, result.getProducts().size());
    }
    //Проверява дали добавя правилна отстъпка към OrderDto
    @Test
    void mapFromOrderToDto_shouldSetCorrectDiscount_whenDiscountIsPresent() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        Discount discount = new Discount();
        discount.setDiscountAmount(BigDecimal.valueOf(0.2));
        order.addDiscount(discount);
        User client = new User();
        client.setName("Ivan");
        order.setClient(client);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza Place");
        order.setRestaurant(restaurant);

        when(modelMapper.map(order, OrderDto.class)).thenReturn(new OrderDto());

        OrderDto result = orderMapper.mapFromOrderToDto(order);

        assertEquals("20%", result.getDiscount());
    }
    //Проверява дали supplierName e null, когато нямаме все още доставчик
    @Test
    void mapFromOrderToDto_shouldSetSupplierNameToNull_whenSupplierIsNull() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        User client = new User();
        client.setName("Ivan");
        order.setClient(client);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza Place");
        order.setRestaurant(restaurant);

        when(modelMapper.map(order, OrderDto.class)).thenReturn(new OrderDto());

        OrderDto result = orderMapper.mapFromOrderToDto(order);

        assertNull(result.getSupplierName());
    }
    //Проверява дали products e празен списък, когато няма поръчани неща
    @Test
    void mapFromOrderToDto_shouldReturnEmptyProducts_whenNoOrderedItems() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        User client = new User();
        client.setName("Ivan");
        order.setClient(client);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza Place");
        order.setRestaurant(restaurant);

        when(modelMapper.map(order, OrderDto.class)).thenReturn(new OrderDto());

        OrderDto result = orderMapper.mapFromOrderToDto(order);

        assertEquals(0, result.getProducts().size());
    }

    //toResponseDto
    //Проверява дали правилно мапва всички полета
    @Test
    void toResponseDto_shouldMapAllFieldsCorrectly_whenOrderIsValid() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.DELIVERED);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Gourmet Burger");
        order.setRestaurant(restaurant);
        User client = new User();
        client.setPhoneNumber("0888123456");
        Address address = new Address();
        address.setCountry("Bulgaria");
        address.setCity("Sofia");
        address.setStreet("Main Street");
        address.setPostalCode("1000");
        client.addAddress(address);
        order.setClient(client);
        Discount discount = new Discount();
        discount.setDiscountAmount(BigDecimal.valueOf(0.2));
        order.addDiscount(discount);

        OrderResponseDto dto = new OrderResponseDto();
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(dto);
        when(addressMapper.toDto(address)).thenReturn(new AddressDto("Main Street", "Sofia", "1000", "Bulgaria"));

        OrderResponseDto result = orderMapper.toResponseDto(order);

        assertEquals("Gourmet Burger", result.getRestaurantName());
        assertEquals(OrderStatus.DELIVERED.name(), result.getOrderStatus());
        assertEquals("Country: Bulgaria, City: Sofia, Street: Main Street", result.getClientAddress());
        assertEquals("0888123456", result.getClientPhone());
        assertEquals("20%", result.getDiscount());
    }
    //Проверява дали discount остава null, когато няма отстъпка
    @Test
    void toResponseDto_shouldLeaveDiscountNull_whenNoDiscount() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizzeria Roma");
        order.setRestaurant(restaurant);
        User client = new User();
        client.setPhoneNumber("0888123456");
        Address address = new Address();
        address.setCountry("Bulgaria");
        address.setCity("Sofia");
        address.setStreet("Main Street");
        address.setPostalCode("1000");
        client.addAddress(address);
        order.setClient(client);

        OrderResponseDto dto = new OrderResponseDto();
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(dto);
        when(addressMapper.toDto(address)).thenReturn(new AddressDto("Main Street", "Sofia", "1000", "Bulgaria"));

        OrderResponseDto result = orderMapper.toResponseDto(order);

        assertNull(result.getDiscount());
    }
    //Проверява дали хвърля изключение, ако клиентът няма адрес
    @Test
    void toResponseDto_shouldThrowException_whenClientHasNoAddress() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        User client = new User();
        order.setClient(client);

        OrderResponseDto dto = new OrderResponseDto();
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(dto);

        assertThrows(IllegalStateException.class, () -> orderMapper.toResponseDto(order));
    }
    //Проверява дали полето orderStatus се сетва успешно, ако е null в DTO-то
    @Test
    void toResponseDto_shouldSetOrderStatus_whenOrderStatusIsNullInDto() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPTED);
        User client = new User();
        Address address = new Address();
        address.setCountry("Bulgaria");
        address.setCity("Plovdiv");
        address.setStreet("Central Blvd");
        address.setPostalCode("4000");
        client.addAddress(address);
        order.setClient(client);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderStatus(null);

        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(dto);
        when(addressMapper.toDto(address)).thenReturn(new AddressDto("Central Blvd", "Plovdiv", "4000", "Bulgaria"));

        OrderResponseDto result = orderMapper.toResponseDto(order);

        assertEquals(OrderStatus.ACCEPTED.name(), result.getOrderStatus());
    }
}
