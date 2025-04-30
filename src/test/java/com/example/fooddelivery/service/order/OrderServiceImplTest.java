package com.example.fooddelivery.service.order;

import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.entity.role.Role;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.entity.order.Order;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.exception.order.InvalidOrderStatusException;
import com.example.fooddelivery.exception.order.InvalidOrderSupplierException;
import com.example.fooddelivery.exception.role.InvalidRoleException;
import com.example.fooddelivery.mapper.address.AddressMapper;
import com.example.fooddelivery.mapper.order.OrderMapper;
import com.example.fooddelivery.repository.*;
import com.example.fooddelivery.service.discount.DiscountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.fooddelivery.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.restaurant.Restaurant;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private AddressRepository addressRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private DiscountService discountService;
    @Mock private ProductRepository productRepository;
    @Mock private AddressMapper addressMapper;


    @InjectMocks
    private OrderServiceImpl orderService;

    //createOrder
    // Проверява дали при валиден клиент и входни данни, поръчката се създава успешно и връща коректно OrderDto
    @Test
    void createOrder_shouldReturnOrderDto_whenInputIsValid() {
        Long clientId = 1L;
        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setRestaurantId(1L);
        User client = mock(User.class);
        when(client.getId()).thenReturn(clientId);
        Address address = new Address();
        when(addressMapper.mapToAddress(any())).thenReturn(address);
        when(addressRepository.findByStreetAndCityAndCountryAndUserId(any(), any(), any(), any()))
                .thenReturn(Optional.of(address));
        when(client.getAddresses()).thenReturn(Set.of(address));
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        Role clientRole = new Role();
        clientRole.setName("CLIENT");
        when(client.getRole()).thenReturn(clientRole);
        when(discountService.checkAndGiveClientDiscount(any())).thenReturn(null);
        createDto.setProducts(Set.of());

        Order order = new Order();
        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(123L);

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.mapFromOrderToDto(any(Order.class))).thenReturn(expectedDto);

        OrderDto result = orderService.createOrder(createDto, clientId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        verify(orderRepository, atLeastOnce()).save(any(Order.class));
        verify(orderMapper).mapFromOrderToDto(any(Order.class));
    }
    // Проверява дали при липсващ клиент, методът хвърля EntityNotFoundException и не записва поръчка
    @Test
    void createOrder_shouldThrowEntityNotFoundException_whenClientNotFound() {
        Long clientId = 99L;
        OrderCreateDto createDto = new OrderCreateDto();
        when(userRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(createDto, clientId));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderMapper, never()).mapFromOrderToDto(any(Order.class));
    }

    //assignOrderToSupplier
    //Тест за успешно присвояване на доставка към доставчик
    @Test
    void assignOrderToSupplier_shouldAssignSuccessfully_whenOrderAndSupplierExist() {
        Long orderId = 10L;
        Long supplierId = 5L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPTED);
        User supplier = mock(User.class);
        Role supplierRole = new Role();
        supplierRole.setName("SUPPLIER");
        when(supplier.getRole()).thenReturn(supplierRole);

        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.mapFromOrderToDto(order)).thenReturn(expectedDto);

        OrderDto result = orderService.assignOrderToSupplier(orderId, supplierId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        verify(orderRepository).findById(orderId);
        verify(userRepository).findById(supplierId);
        verify(orderRepository).save(order);
        verify(orderMapper).mapFromOrderToDto(order);
    }
    @Test
    void assignOrderToSupplier_shouldThrowEntityNotFoundException_whenOrderNotFound() {
        Long orderId = 10L;
        Long supplierId = 5L;

        assertThrows(EntityNotFoundException.class, () -> orderService.assignOrderToSupplier(orderId, supplierId));

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    //Тест ако Supplier-a не съществува
    @Test
    void assignOrderToSupplier_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        Long orderId = 10L;
        Long supplierId = 5L;

        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.assignOrderToSupplier(orderId, supplierId));
        verify(orderRepository, never()).save(any(Order.class));
    }

    //acceptOrder
    // Позитивен тест, дали всичко е топ ако и ордъра и доставчика съществува
    @Test
    void acceptOrder_shouldAcceptSuccessfully_whenOrderAndSupplierExist() {
        Long orderId = 20L;
        Long supplierId = 7L;
        Order order = new Order();

        // Mock User and Role
        User supplier = mock(User.class);
        Role supplierRole = mock(Role.class);

        when(supplier.getRole()).thenReturn(supplierRole);
        when(supplierRole.getName()).thenReturn("EMPLOYEE");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        orderService.acceptOrder(orderId, supplierId);

        verify(orderRepository).findById(orderId);
        verify(userRepository).findById(supplierId);
        verify(orderRepository).save(order);
    }


    // Тест, когато order not found
    @Test
    void acceptOrder_shouldThrowEntityNotFoundException_whenOrderNotFound() {
        Long orderId = 20L;
        Long supplierId = 7L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.acceptOrder(orderId, supplierId));
        verify(userRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }
    // Тест ако доставчика не съществува
    @Test
    void acceptOrder_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        Long orderId = 20L;
        Long supplierId = 7L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.acceptOrder(orderId, supplierId));
        verify(orderRepository, never()).save(any(Order.class));
    }

    //updateOderStatus
    //Тест когато се опитваме да обновим статуса на Order, който не съществува
    @Test
    void updateOrderStatus_shouldThrowEntityNotFoundException_whenOrderNotFound() {
        Long orderId = 30L;
        Long supplierId = 8L;
        User supplier = new User();
        Role supplierRole = new Role();
        supplierRole.setName("EMPLOYEE");
        supplier.setRole(supplierRole);

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderStatus(orderId, supplierId));

        verify(userRepository).findById(supplierId);
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    //Тества дали статуса се променя правилно
    @Test
    void updateOrderStatus_shouldSetStatusToPreparing_whenCurrentStatusIsAccepted() {
        Long orderId = 30L;
        Long employeeId = 8L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPTED);

        User employee = new User();
        employee.setRole(new Role());
        employee.getRole().setName("EMPLOYEE");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrderStatus(orderId, employeeId);

        assertEquals(OrderStatus.PREPARING, order.getOrderStatus());
    }
    @Test
    void updateOrderStatus_shouldThrowInvalidRoleException_whenUserIsNotEmployee() {
        Long orderId = 30L;
        Long userId = 8L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPTED);
        User user = new User();
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        lenient().when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(InvalidRoleException.class, () -> orderService.updateOrderStatus(orderId, userId));
    }
    //Supplier не съществува
    @Test
    void updateOrderStatus_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        Long orderId = 30L;
        Long supplierId = 8L;

        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderStatus(orderId, supplierId));

        verify(orderRepository, never()).save(any(Order.class));
    }

    //takeOrder
    //Тества когато takeOrder се реализира успешно
    @Test
    void takeOrder_shouldTakeSuccessfully_whenOrderAndSupplierExist() {
        Long orderId = 40L;
        Long supplierId = 9L;
        Order order = new Order();
        User supplier = mock(User.class);
        Role role = new Role();
        role.setName("SUPPLIER");

        when(supplier.getRole()).thenReturn(role);
        when(supplier.getId()).thenReturn(supplierId);
        order.setSupplier(supplier);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.takeOrder(orderId, supplierId);

        verify(orderRepository).findById(orderId);
        verify(userRepository).findById(supplierId);
        verify(orderRepository).save(order);

        assertEquals(OrderStatus.IN_DELIVERY, order.getOrderStatus());
    }
    // Поръчка не съществува
    @Test
    void takeOrder_shouldThrowEntityNotFoundException_whenOrderNotFound() {
        Long orderId = 40L;
        Long supplierId = 9L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.takeOrder(orderId, supplierId));
        verify(userRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }
    //Суплиер не съществува
    @Test
    void takeOrder_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        Long orderId = 40L;
        Long supplierId = 9L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.takeOrder(orderId, supplierId));
        verify(orderRepository, never()).save(any(Order.class));
    }
    @Test
    void takeOrder_shouldThrowException_whenOrderIsAssignedToDifferentSupplier() {
        Long orderId = 40L;
        Long actualSupplierId = 100L;
        Long anotherSupplierId = 9L;
        User actualSupplier = mock(User.class);
        when(actualSupplier.getId()).thenReturn(actualSupplierId);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setSupplier(actualSupplier);
        User anotherSupplier = mock(User.class);
        Role supplierRole = new Role();
        supplierRole.setName("SUPPLIER");
        when(anotherSupplier.getRole()).thenReturn(supplierRole);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(anotherSupplierId)).thenReturn(Optional.of(anotherSupplier));

        assertThrows(InvalidOrderSupplierException.class, () -> orderService.takeOrder(orderId, anotherSupplierId));
    }
    // Проверява дали статуса се update-ва както трябва
    @Test
    void takeOrder_shouldSetOrderStatusToInDelivery_whenSuccessful() {
        Long orderId = 40L;
        Long supplierId = 9L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPTED);
        User supplier = mock(User.class);
        when(supplier.getId()).thenReturn(supplierId);
        Role role = new Role();
        role.setName("SUPPLIER");
        when(supplier.getRole()).thenReturn(role);
        order.setSupplier(supplier);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.takeOrder(orderId, supplierId);

        assertEquals(OrderStatus.IN_DELIVERY, order.getOrderStatus());
    }

    //finishOrder
    //Успешно завършване на поръчка от доставчик
    @Test
    void finishOrder_shouldFinishSuccessfully_whenOrderAndSupplierExist() {
        Long orderId = 50L;
        Long supplierId = 10L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.IN_DELIVERY);

        User supplier = mock(User.class);
        Role role = new Role();
        role.setName("SUPPLIER");
        when(supplier.getRole()).thenReturn(role);

        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.mapFromOrderToDto(order)).thenReturn(expectedDto);

        OrderDto result = orderService.finishOrder(orderId, supplierId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(OrderStatus.DELIVERED, order.getOrderStatus());
    }
    //Тест при несъщствуващи order
    @Test
    void finishOrder_shouldThrowEntityNotFoundException_whenOrderNotFound() {
        Long orderId = 50L;
        Long supplierId = 10L;
        User supplier = mock(User.class);
        Role role = new Role();
        role.setName("SUPPLIER");

        when(supplier.getRole()).thenReturn(role);
        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.finishOrder(orderId, supplierId));
    }
    //Тест при несъщствуващи supplier
    @Test
    void finishOrder_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        Long orderId = 50L;
        Long supplierId = 10L;

        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());
        lenient().when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));

        assertThrows(EntityNotFoundException.class, () -> orderService.finishOrder(orderId, supplierId));
    }
    //Тест при опит за завършване на вече завършена поръчка
    @Test
    void finishOrder_shouldThrowException_whenOrderAlreadyDelivered() {
        Long orderId = 50L;
        Long supplierId = 10L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.DELIVERED);

        User supplier = mock(User.class);
        Role role = new Role();
        role.setName("SUPPLIER");
        when(supplier.getRole()).thenReturn(role);

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class, () -> orderService.finishOrder(orderId, supplierId));
    }


    //cancelOrderByClient!
    //Успешно отказване на поръчка от клиент
    @Test
    void cancelOrderByClient_shouldCancelSuccessfully_whenOrderAndClientExist() {
        Long orderId = 60L;
        Long clientId = 11L;
        User client = mock(User.class);
        Role role = new Role();
        role.setName("CLIENT");
        when(client.getId()).thenReturn(clientId);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setClient(client);
        OrderDto expectedDto = new OrderDto();
        expectedDto.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.mapFromOrderToDto(order)).thenReturn(expectedDto);

        OrderDto result = orderService.cancelOrderByClient(orderId, clientId);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }

    //Тестове при несъщствуващи order и supplier
    @Test
    void cancelOrderByClient_shouldThrowEntityNotFoundException_whenOrderNotFound() {
        Long orderId = 60L;
        Long clientId = 11L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrderByClient(orderId, clientId));
    }
    @Test
    void cancelOrderByClient_shouldThrowEntityNotFoundException_whenClientNotFound() {
        Long orderId = 60L;
        Long clientId = 11L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrderByClient(orderId, clientId));
    }
    //Поръчката вече е доставена, не може да се отмени, тест против измекяри
    @Test
    void cancelOrderByClient_shouldThrowException_whenOrderAlreadyDelivered() {
        Long orderId = 60L;
        Long clientId = 11L;

        User client = mock(User.class);
        when(client.getId()).thenReturn(clientId);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setClient(client);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));

        assertThrows(InvalidOrderStatusException.class, () -> orderService.cancelOrderByClient(orderId, clientId));
    }

    //getOrdersByClient
    //Клиента не съществува
    @Test
    void getOrdersByClient_shouldReturnEmptyList_whenNoOrdersFound() {
        Long clientId = 12L;

        when(orderRepository.findByClientId(clientId)).thenReturn(List.of());

        List<OrderResponseDto> result = orderService.getOrdersByClient(clientId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    //Клиента няма поръчки
    @Test
    void getOrdersByClient_shouldReturnEmptyList_whenClientHasNoOrders() {
        Long clientId = 12L;

        when(orderRepository.findByClientId(clientId)).thenReturn(List.of());

        List<OrderResponseDto> result = orderService.getOrdersByClient(clientId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //getOrdersBySupplier
    //връща списък с поръчки за доставчика
    @Test
    void getOrdersBySupplier_shouldReturnListOfOrders_whenSupplierExists() {
        Long supplierId = 13L;
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        OrderResponseDto dto1 = new OrderResponseDto();
        dto1.setOrderStatus("DELIVERED");
        dto1.setTotalPrice(BigDecimal.TEN);
        OrderResponseDto dto2 = new OrderResponseDto();
        dto2.setOrderStatus("PENDING");
        dto2.setTotalPrice(BigDecimal.ONE);
        List<Order> orders = List.of(order1, order2);
        List<OrderResponseDto> expectedDtos = List.of(dto1, dto2);

        when(orderRepository.findBySupplierId(supplierId)).thenReturn(orders);
        when(orderMapper.toResponseDto(order1)).thenReturn(dto1);
        when(orderMapper.toResponseDto(order2)).thenReturn(dto2);

        List<OrderResponseDto> result = orderService.getOrdersBySupplier(supplierId);

        assertEquals(expectedDtos.size(), result.size());
        assertTrue(result.containsAll(expectedDtos));
    }
    //Supplier няма поръчки
    @Test
    void getOrdersBySupplier_shouldReturnEmptyList_whenSupplierHasNoOrders() {
        Long supplierId = 13L;

        when(orderRepository.findBySupplierId(supplierId)).thenReturn(List.of());

        List<OrderResponseDto> result = orderService.getOrdersBySupplier(supplierId);

        assertTrue(result.isEmpty());
    }

    //getOrdersByStatus
    //връща списък с поръчки със статус и служител
    @Test
    void getOrdersByStatus_shouldReturnListOfOrders_whenStatusAndEmployeeExist() {
        OrderStatus status = OrderStatus.IN_DELIVERY;
        Long employeeId = 14L;
        User employee = mock(User.class);
        when(employee.getRole()).thenReturn(new Role("EMPLOYEE"));
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = List.of(order1, order2);
        OrderResponseDto dto1 = new OrderResponseDto();
        OrderResponseDto dto2 = new OrderResponseDto();

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(orderRepository.findByOrderStatus(status)).thenReturn(orders);
        when(orderMapper.toResponseDto(order1)).thenReturn(dto1);
        when(orderMapper.toResponseDto(order2)).thenReturn(dto2);

        List<OrderResponseDto> result = orderService.getOrdersByStatus(status, employeeId);

        assertEquals(2, result.size());
    }


    //Supplier не съществува
    @Test
    void getOrdersByStatus_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        OrderStatus status = OrderStatus.IN_DELIVERY;
        Long supplierId = 14L;

        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrdersByStatus(status, supplierId));
    }
    //Няма поръчки с такъв статус
    @Test
    void getOrdersByStatus_shouldReturnEmptyList_whenNoOrdersWithStatus() {
        OrderStatus status = OrderStatus.IN_DELIVERY;
        Long employeeId = 14L;
        User employee = mock(User.class);
        Role role = new Role();
        role.setName("EMPLOYEE");
        when(employee.getRole()).thenReturn(role);

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(orderRepository.findByOrderStatus(status)).thenReturn(List.of());
        List<OrderResponseDto> result = orderService.getOrdersByStatus(status, employeeId);

        assertTrue(result.isEmpty());
    }

    //getTotalRevenue
    //връща коректна сума за периода
    @Test
    void getTotalRevenueBetween_shouldReturnTotalRevenue_whenAdminExists() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        Long adminId = 15L;
        User admin = mock(User.class);
        Role role = new Role();
        role.setName("ADMIN");
        when(admin.getRole()).thenReturn(role);

        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        when(order1.getTotalPrice()).thenReturn(BigDecimal.valueOf(100));
        when(order2.getTotalPrice()).thenReturn(BigDecimal.valueOf(200));
        List<Order> orders = List.of(order1, order2);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(orderRepository.findByCreatedAtBetween(from, to)).thenReturn(orders);

        BigDecimal result = orderService.getTotalRevenueBetween(from, to, adminId);

        assertEquals(BigDecimal.valueOf(300), result);
    }

    //Админ не съществува
    @Test
    void getTotalRevenueBetween_shouldThrowEntityNotFoundException_whenAdminNotFound() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        Long adminId = 15L;

        when(userRepository.findById(adminId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getTotalRevenueBetween(from, to, adminId));
    }
    //Няма поръчки в дадения период
    @Test
    void getTotalRevenueBetween_shouldReturnZero_whenNoOrdersInPeriod() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        Long adminId = 15L;
        User admin = mock(User.class);
        Role role = new Role();
        role.setName("ADMIN");
        when(admin.getRole()).thenReturn(role);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(orderRepository.findByCreatedAtBetween(from, to)).thenReturn(List.of());

        BigDecimal result = orderService.getTotalRevenueBetween(from, to, adminId);

        assertEquals(BigDecimal.ZERO, result);
    }
    //Не администратор се опитва да вземе revenue-то
    @Test
    void getTotalRevenueBetween_shouldThrowException_whenUserIsNotAdmin() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        Long adminId = 15L;

        User user = mock(User.class);
        Role role = new Role();
        role.setName("SUPPLIER");
        when(user.getRole()).thenReturn(role);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(user));

        assertThrows(InvalidRoleException.class, () -> orderService.getTotalRevenueBetween(from, to, adminId));
    }

    //getAvailableOrdersForSuppliers
    //Няма такъв доставчикю
    @Test
    void getAvailableOrdersForSuppliers_shouldThrowEntityNotFoundException_whenSupplierNotFound() {
        Long supplierId = 16L;

        when(userRepository.findById(supplierId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getAvailableOrdersForSuppliers(supplierId));
    }
    //Няма налични поръчки
    @Test
    void getAvailableOrdersForSuppliers_shouldReturnEmptyList_whenNoOrdersAvailable() {
        Long supplierId = 16L;
        User supplier = new User();
        Role role = new Role();
        role.setName("SUPPLIER");
        supplier.setRole(role);

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.PREPARING)).thenReturn(new ArrayList<>());
        when(orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.ACCEPTED)).thenReturn(new ArrayList<>());

        List<OrderResponseDto> result = orderService.getAvailableOrdersForSuppliers(supplierId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    //Поръчка няма статус
    @Test
    void getAvailableOrdersForSuppliers_shouldHandleOrderWithNullStatus() {
        Long supplierId = 16L;
        User supplier = new User();
        Role role = new Role();
        role.setName("SUPPLIER");
        supplier.setRole(role);
        Order order = new Order();
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderStatus("UNKNOWN");

        when(userRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.PREPARING)).thenReturn(new ArrayList<>(List.of(order)));
        when(orderRepository.findByOrderStatusAndSupplierIsNull(OrderStatus.ACCEPTED)).thenReturn(new ArrayList<>());
        when(orderMapper.toResponseDto(order)).thenReturn(dto);

        List<OrderResponseDto> result = orderService.getAvailableOrdersForSuppliers(supplierId);

        assertEquals(1, result.size());
        assertEquals("UNKNOWN", result.get(0).getOrderStatus());
    }

}
