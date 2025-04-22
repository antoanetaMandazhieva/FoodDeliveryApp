package com.example.fooddelivery.service.user;

import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.repository.UserRepository;
import com.example.fooddelivery.entity.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getUserById_shouldReturnUserProfileDto_whenUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        UserProfileDto dto = new UserProfileDto();
        dto.setUsername("testUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserProfileDto(user)).thenReturn(dto);

        UserProfileDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository).findById(userId);
        verify(userMapper).mapToUserProfileDto(user);
    }
    @Test
    void getUserById_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
    }
}
