package com.example.fooddelivery.service.user;

import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {

    UserProfileDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserProfileDto updateUser(Long id, UserProfileDto dto);

    void deleteUser(Long id);

    void changeUserRole(Long adminId, Long userId, String newRole);

    Long getUserIdFromUsername(String username);

    List<OrderResponseDto> getOrdersByClientUsername(String clientUsername);

    List<OrderResponseDto> getOrdersBySupplierUsername(String supplierUsername, Long workerId);

    Long getSupplierIdByUsername(String username);
}