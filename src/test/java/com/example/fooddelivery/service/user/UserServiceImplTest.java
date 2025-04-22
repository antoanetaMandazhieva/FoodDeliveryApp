package com.example.fooddelivery.service.user;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.Gender;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.RoleRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;

    private AddressMapper addressMapper;
    private UserMapper userMapper;

    private UserServiceImpl userService;
    private boolean useRealMappers = false;

    @BeforeEach
    void baseSetUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderMapper = mock(OrderMapper.class);
    }

    void setUpWithRealMappers() {
        addressMapper = new AddressMapper(new ModelMapper());
        userMapper = new UserMapper(new ModelMapper(), addressMapper);

        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                orderRepository,
                userMapper,
                orderMapper,
                addressMapper
        );
    }

    void setUpWithMockMappers() {
        addressMapper = mock(AddressMapper.class);
        userMapper = mock(UserMapper.class);

        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                orderRepository,
                userMapper,
                orderMapper,
                addressMapper
        );
    }

    @Test
    void getUserById_shouldReturnUserProfileDto_whenUserExists() {
        // Arrange
        setUpWithMockMappers();
        Long userId = 1L;

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername("testUser");
        dto.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserProfileDto(user)).thenReturn(dto);

        // Act
        UserProfileDto result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).findById(userId);
        verify(userMapper).mapToUserProfileDto(user);
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        // Arrange
        setUpWithMockMappers();
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(userId));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getAllUsers_shouldReturnMappedUserDtos() {
        setUpWithRealMappers();
        Role userRole = new Role();
        userRole.setName("USER");

        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        User user1 = new User("a@example.com", "pass1", "user1", "Alice", "Smith", Gender.FEMALE, LocalDate.of(2000, 1, 1), "123456789");
        user1.setRole(userRole);

        User user2 = new User("b@example.com", "pass2", "user2", "Bob", "Jones", Gender.MALE, LocalDate.of(1995, 2, 2), "987654321");
        user2.setRole(adminRole);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("USER", result.get(0).getRole());
        assertEquals("user2", result.get(1).getUsername());
        assertEquals("ADMIN", result.get(1).getRole());

        verify(userRepository).findAll();
    }
    @Test
    void updateUser_shouldUpdateFieldsAndAddNewAddress() {
        setUpWithMockMappers(); // <-- Използва mock мапъри

        Long userId = 1L;
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("New Street");
        addressDto.setCity("New City");

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername("newUsername");
        dto.setEmail("new@example.com");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setPhoneNumber("1234567890");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender(Gender.MALE);
        dto.setAddresses(Set.of(addressDto));

        User existingUser = new User();
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("old@example.com");
        existingUser.setName("Old");
        existingUser.setSurname("User");
        existingUser.setPhoneNumber("0987654321");
        existingUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        existingUser.setGender(Gender.FEMALE);
        existingUser.setRole(new Role());
        Address existingAddress = new Address();
        existingAddress.setStreet("Old Street");
        existingAddress.setCity("Old City");
        existingUser.addAddress(existingAddress);

        Address mappedAddress = new Address();
        mappedAddress.setStreet("New Street");
        mappedAddress.setCity("New City");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(addressMapper.mapToAddress(addressDto)).thenReturn(mappedAddress);
        when(userMapper.mapToUserProfileDto(any(User.class))).thenReturn(dto);

        UserProfileDto updated = userService.updateUser(userId, dto);

        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals(Gender.MALE, existingUser.getGender());
        assertTrue(existingUser.getAddresses().contains(mappedAddress));
        verify(userRepository).save(existingUser);
        assertEquals(dto, updated);
    }
    @Test
    void updateUser_shouldThrowIfUserNotFound() {
        setUpWithMockMappers();
        Long userId = 100L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(userId, new UserProfileDto())
        );

        assertEquals("User not found", exception.getMessage());
    }
    @Test
    void updateUser_shouldThrowIfUsernameTaken() {
        setUpWithMockMappers();
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUsername("oldUsername");

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername("takenUsername");
        dto.setEmail("same@example.com");
        dto.setPhoneNumber("123");

        User otherUser = new User();
        otherUser.setUsername("takenUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("takenUsername")).thenReturn(Optional.of(otherUser));

        // When / Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(userId, dto)
        );
        assertEquals("Username is already taken", ex.getMessage());
    }
    @Test
    void updateUser_shouldNotAddDuplicateAddress() {
        setUpWithMockMappers();
        Long userId = 1L;

        AddressDto dtoAddress = new AddressDto();
        dtoAddress.setStreet("Same Street");
        dtoAddress.setCity("Same City");

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername("user");
        dto.setEmail("email@example.com");
        dto.setPhoneNumber("123456");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender(Gender.MALE);
        dto.setAddresses(Set.of(dtoAddress));

        User user = new User();
        user.setUsername("user");
        user.setEmail("email@example.com");
        user.setPhoneNumber("123456");
        user.setName("John");
        user.setSurname("Doe");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setGender(Gender.MALE);

        Address sameAddress = new Address();
        sameAddress.setStreet("Same Street");
        sameAddress.setCity("Same City");
        user.addAddress(sameAddress);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(addressMapper.mapToAddress(any())).thenReturn(sameAddress);
        when(userMapper.mapToUserProfileDto(any())).thenReturn(dto);

        // When
        UserProfileDto result = userService.updateUser(userId, dto);

        // Then
        assertEquals(1, user.getAddresses().size());
        verify(userRepository).save(user);
        assertEquals(dto, result);
    }
}
