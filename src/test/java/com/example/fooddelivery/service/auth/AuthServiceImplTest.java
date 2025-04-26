package com.example.fooddelivery.service.auth;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.RoleRepository;
import com.example.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //Провервяа дали човек успешно се регистрира и дали данните му се попълват в БД
    @Test
    void register_shouldSaveUser_whenValidData() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("newUser");
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("1234567890");
        AddressDto addressDto = new AddressDto();
        dto.setAddress(addressDto);
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("new@example.com");
        user.setPhoneNumber("1234567890");
        Address address = new Address();
        Role role = new Role();
        role.setName("CLIENT");

        when(userMapper.mapToUser(dto)).thenReturn(user);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.empty());
        when(roleRepository.findByName("CLIENT")).thenReturn(Optional.of(role));
        when(addressMapper.mapToAddress(addressDto)).thenReturn(address);

        authService.register(dto);

        verify(userRepository).save(user);
    }
    //Проверява дали вече има регистриран профил с такъв username в БД
    @Test
    void register_shouldThrow_whenUsernameExists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("existingUser");
        User user = new User();
        user.setUsername("existingUser");

        when(userMapper.mapToUser(dto)).thenReturn(user);
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(dto));

        assertEquals("Username is already taken.", exception.getMessage());
    }
    //Проверява дали вече има регистриран профил с такъв email в БД
    @Test
    void register_shouldThrow_whenEmailExists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("newUser");
        dto.setEmail("used@example.com");
        User user = new User();
        user.setUsername("newUser");

        when(userMapper.mapToUser(dto)).thenReturn(user);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("used@example.com")).thenReturn(Optional.of(user));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(dto));

        assertEquals("Email is already taken.", exception.getMessage());
    }
    // Проверява дали вече има регистриран профил с такъв phone number в БД
    @Test
    void register_shouldThrow_whenPhoneNumberExists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("newUser");
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("1234567890");
        User user = new User();
        user.setUsername("newUser");

        when(userMapper.mapToUser(dto)).thenReturn(user);
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(user));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(dto));

        assertEquals("Phone number is already taken.", exception.getMessage());
    }
    // Проверява какво се случва ако въведен грешен username
    @Test
    void login_shouldThrow_whenUsernameIsInvalid() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("unknownUser");
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));

        assertEquals("Invalid username.", exception.getMessage());

        verify(userRepository).findByUsername("unknownUser");
    }
    // Проверява какво се случва ако въведен грешна парола
    @Test
    void login_shouldThrow_whenPasswordIsIncorrect() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");
        User user = mock(User.class);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(user.checkPassword("wrongPassword")).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));

        assertEquals("Invalid password.", exception.getMessage());

        verify(userRepository).findByUsername("testUser");
        verify(user).checkPassword("wrongPassword");
    }
}
