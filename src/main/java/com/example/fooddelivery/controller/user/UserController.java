package com.example.fooddelivery.controller.user;

import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/id")
    public ResponseEntity<UserProfileDto> deleteUser(@PathVariable Long id,
                                           @RequestBody UserProfileDto userProfileDto) {
        return ResponseEntity.ok(userService.updateUser(id, userProfileDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{adminId}/change-role/{userId}")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long adminId,
                                               @PathVariable Long userId,
                                               @RequestParam String role) throws AccessDeniedException {
        userService.changeUserRole(adminId, userId, role);

        return ResponseEntity.ok().build();
    }

    @GetMapping("orders/client/{username}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByClientUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getOrdersByClientUsername(username));
    }

    @GetMapping("orders/supplier/{username}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersBySupplierName(@PathVariable String username) {
        return ResponseEntity.ok(userService.getOrdersBySupplierUsername(username));
    }
}