package com.example.fooddelivery.controller.user;

import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    //getUserById
    //Тества дали връща коректния профил при съществуващ потребител
    @Test
    void getUserById_shouldReturnUserProfile_whenUserExists() throws Exception {
        Long userId = 1L;
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUsername("testUser");

        when(userService.getUserById(userId)).thenReturn(userProfileDto);

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }
    //Проверява дали връща грешка при несъществуващ потребител
    @Test
    void getUserById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        Long userId = 1L;

        when(userService.getUserById(userId))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //getAllUsers
    //Тества дали връща списък с всички потребители
    @Test
    void getAllUsers_shouldReturnListOfUsers_whenUsersExist() throws Exception {
        UserDto user1 = new UserDto();
        user1.setUsername("testUser1");
        UserDto user2 = new UserDto();
        user2.setUsername("testUser2");

        List<UserDto> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andExpect(jsonPath("$[0].username").value("testUser1"))
                .andExpect(jsonPath("$[1].username").value("testUser2"));
    }
    //Проверява дали връща празен списък, когато няма потребители
    @Test
    void getAllUsers_shouldReturnEmptyList_whenNoUsersExist() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    //updateUser
    //Тества дали обновява профила на потребителя успешно
    @Test
    void updateUser_shouldReturnUpdatedUserProfile_whenInputIsValid() throws Exception {
        Long userId = 1L;
        UserProfileDto inputDto = new UserProfileDto();
        inputDto.setUsername("updatedUser");

        UserProfileDto updatedDto = new UserProfileDto();
        updatedDto.setUsername("updatedUser");

        when(userService.updateUser(eq(userId), any(UserProfileDto.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    //Тества дали връща грешка при опит за обновяване на несъществуващ потребител
    @Test
    void updateUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        Long userId = 1L;
        UserProfileDto inputDto = new UserProfileDto();
        inputDto.setUsername("updatedUser");

        when(userService.updateUser(eq(userId), any(UserProfileDto.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isNotFound());
    }

    //deleteUser
    //Тества дали успешно изтрива потребител
    @Test
    void deleteUser_shouldReturnOk_whenUserDeletedSuccessfully() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    //Проверява дали връща грешка при опит за изтриване на несъществуващ потребител
    @Test
    void deleteUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        Long userId = 1L;

        doThrow(new EntityNotFoundException("User not found"))
                .when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //changeUserRole
    //Проверява дали успешно се сменя ролята на потребител
    @Test
    void changeUserRole_shouldReturnOk_whenRoleChangedSuccessfully() throws Exception {
        Long adminId = 1L;
        Long userId = 2L;
        String role = "SUPPLIER";

        doNothing().when(userService).changeUserRole(adminId, userId, role);

        mockMvc.perform(put("/api/users/{adminId}/change-role/{userId}", adminId, userId)
                        .param("role", role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully changed role for User with ID: 2. New role: SUPPLIER."));
    }

    //Проверява дали връща грешка ако user не съществува
    @Test
    void changeUserRole_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        Long adminId = 1L;
        Long userId = 2L;
        String role = "SUPPLIER";

        doThrow(new EntityNotFoundException("User not found"))
                .when(userService).changeUserRole(adminId, userId, role);

        mockMvc.perform(put("/api/users/{adminId}/change-role/{userId}", adminId, userId)
                        .param("role", role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    //Проверява дали спира user-а ако не е админ
    @Test
    void changeUserRole_shouldReturnForbidden_whenAccessDenied() throws Exception {
        Long adminId = 1L;
        Long userId = 2L;
        String role = "SUPPLIER";

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied"))
                .when(userService).changeUserRole(adminId, userId, role);

        mockMvc.perform(put("/api/users/{adminId}/change-role/{userId}", adminId, userId)
                        .param("role", role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    //getOrdersByClientUsername
    //Тества дали връща списък с поръчки за съществуващ клиент
    @Test
    void getOrdersByClientUsername_shouldReturnOrders_whenClientExists() throws Exception {
        String username = "clientUser";
        OrderResponseDto order1 = new OrderResponseDto();
        OrderResponseDto order2 = new OrderResponseDto();
        List<OrderResponseDto> orders = List.of(order1, order2);

        when(userService.getOrdersByClientUsername(username)).thenReturn(orders);

        mockMvc.perform(get("/api/users/orders/client/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orders.size()));
    }
    //Проверява дали връща грешка при несъществуващ клиент
    @Test
    void getOrdersByClientUsername_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        String username = "unknownClient";

        when(userService.getOrdersByClientUsername(username))
                .thenThrow(new EntityNotFoundException("Client not found"));

        mockMvc.perform(get("/api/users/orders/client/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //getOrdersBySupplierName
    //Тества дали връща списък с поръчки за съществуващ доставчик
    @Test
    void getOrdersBySupplierName_shouldReturnOrders_whenSupplierExists() throws Exception {
        String username = "supplierUser";
        Long workerId = 3L;
        OrderResponseDto order1 = new OrderResponseDto();
        OrderResponseDto order2 = new OrderResponseDto();
        List<OrderResponseDto> orders = List.of(order1, order2);

        when(userService.getOrdersBySupplierUsername(username, workerId)).thenReturn(orders);

        mockMvc.perform(get("/api/users/orders/supplier/{username}", username)
                        .param("id", String.valueOf(workerId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orders.size()));
    }
    //Проверява дали връща грешка при несъществуващ доставчик
    @Test
    void getOrdersBySupplierName_shouldReturnNotFound_whenSupplierDoesNotExist() throws Exception {
        String username = "unknownSupplier";
        Long workerId = 3L;

        when(userService.getOrdersBySupplierUsername(username, workerId))
                .thenThrow(new EntityNotFoundException("Supplier not found"));

        mockMvc.perform(get("/api/users/orders/supplier/{username}", username)
                        .param("id", String.valueOf(workerId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //getSupplierIdByUsername
    //Тества дали връща коректното supplierId при съществуващ доставчик
    @Test
    void getSupplierIdByUsername_shouldReturnSupplierId_whenSupplierExists() throws Exception {
        String username = "supplierUser";
        Long supplierId = 5L;

        when(userService.getSupplierIdByUsername(username)).thenReturn(supplierId);

        mockMvc.perform(get("/api/users/supplier/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(supplierId.toString()));
    }
    //Проверява дали връща грешка при несъществуващ доставчик
    @Test
    void getSupplierIdByUsername_shouldReturnNotFound_whenSupplierDoesNotExist() throws Exception {
        String username = "unknownSupplier";

        when(userService.getSupplierIdByUsername(username))
                .thenThrow(new EntityNotFoundException("Supplier not found"));

        mockMvc.perform(get("/api/users/supplier/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
