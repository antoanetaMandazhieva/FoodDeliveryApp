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

    // Tested!
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Tested!
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Tested!
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateUser(@PathVariable Long id,
                                           @RequestBody UserProfileDto userProfileDto) {
        System.out.println("REACHED PUT ENDPOINT");
        return ResponseEntity.ok(userService.updateUser(id, userProfileDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }


    // Tested!
    @PutMapping("/{adminId}/change-role/{userId}")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long adminId,
                                               @PathVariable Long userId,
                                               @RequestParam String role) throws AccessDeniedException {
        userService.changeUserRole(adminId, userId, role);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/client/{username}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByClientUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getOrdersByClientUsername(username));
    }

    @GetMapping("/orders/supplier/{username}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersBySupplierName(@PathVariable String username) {
        return ResponseEntity.ok(userService.getOrdersBySupplierUsername(username));
    }
}