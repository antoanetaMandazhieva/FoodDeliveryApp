package com.example.fooddelivery.service.discount;

import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.DiscountRepository;
import com.example.fooddelivery.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DiscountServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountServiceImpl discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Проверява, че ако потребителят не е с роля "CLIENT" не може да получи клиентска отстъпка
    @Test
    void checkAndGiveClientDiscount_shouldThrow_whenUserIsNotClient() {
        User user = new User();
        Role role = new Role();
        role.setName("EMPLOYEE");
        user.setRole(role);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> discountService.checkAndGiveClientDiscount(user));
        assertEquals("You cannot have client discount", exception.getMessage());
    }
    //Проверя дали клиент получава правилно отстъпка при 10 поръчки
    @Test
    void checkAndGiveClientDiscount_shouldReturnDiscount_whenOrdersAreMultipleOfTen() {
        User user = new User();
        user.setUsername("testClient");
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        OrderResponseDto orderDto = mock(OrderResponseDto.class);
        List<OrderResponseDto> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orders.add(orderDto);
        }
        when(userService.getOrdersByClientUsername("testClient")).thenReturn(orders);
        Discount savedDiscount = new Discount();
        savedDiscount.setDiscountAmount(BigDecimal.valueOf(0.1));
        when(discountRepository.save(any(Discount.class))).thenReturn(savedDiscount);
        Discount result = discountService.checkAndGiveClientDiscount(user);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0.1), result.getDiscountAmount());
        verify(userService).getOrdersByClientUsername("testClient");
        verify(discountRepository).save(any(Discount.class));
    }
    //Проверява дали връща NULL ако поръчките не са кратни на 10
    @Test
    void checkAndGiveClientDiscount_shouldReturnNull_whenOrdersAreNotMultipleOfTen() {
        User user = new User();
        user.setUsername("testClient");
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        OrderResponseDto orderDto = mock(OrderResponseDto.class);
        List<OrderResponseDto> orders = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            orders.add(orderDto);
        }
        when(userService.getOrdersByClientUsername("testClient")).thenReturn(orders);
        Discount result = discountService.checkAndGiveClientDiscount(user);

        assertNull(result);
        verify(userService).getOrdersByClientUsername("testClient");
        verifyNoInteractions(discountRepository);
    }
    // Проверяват дали функцията връща правилен резултат при всяква роля
    // Admin - 30% Employee - 20% Supplier - 10%
    @Test
    void checkAndGiveWorkerDiscount_shouldReturnDiscountWith30Percent_whenRoleIsAdmin() {
        User user = new User();
        Role role = new Role();
        role.setName("ADMIN");
        user.setRole(role);

        Discount savedDiscount = new Discount();
        savedDiscount.setDiscountAmount(BigDecimal.valueOf(0.3));
        when(discountRepository.save(any(Discount.class))).thenReturn(savedDiscount);
        Discount result = discountService.checkAndGiveWorkerDiscount(user);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0.3), result.getDiscountAmount());
        verify(discountRepository).save(any(Discount.class));
    }
    @Test
    void checkAndGiveWorkerDiscount_shouldReturnDiscountWith20Percent_whenRoleIsEmployee() {
        User user = new User();
        Role role = new Role();
        role.setName("EMPLOYEE");
        user.setRole(role);

        Discount savedDiscount = new Discount();
        savedDiscount.setDiscountAmount(BigDecimal.valueOf(0.2));
        when(discountRepository.save(any(Discount.class))).thenReturn(savedDiscount);
        Discount result = discountService.checkAndGiveWorkerDiscount(user);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0.2), result.getDiscountAmount());
        verify(discountRepository).save(any(Discount.class));
    }

    @Test
    void checkAndGiveWorkerDiscount_shouldReturnDiscountWith10Percent_whenRoleIsSupplier() {
        User user = new User();
        Role role = new Role();
        role.setName("SUPPLIER");
        user.setRole(role);

        Discount savedDiscount = new Discount();
        savedDiscount.setDiscountAmount(BigDecimal.valueOf(0.1));
        when(discountRepository.save(any(Discount.class))).thenReturn(savedDiscount);
        Discount result = discountService.checkAndGiveWorkerDiscount(user);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0.1), result.getDiscountAmount());
        verify(discountRepository).save(any(Discount.class));
    }
}
