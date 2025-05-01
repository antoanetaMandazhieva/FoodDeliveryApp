package com.example.fooddelivery.controller.auth;

import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin_InvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Създаваме заявка с грешни логин данни
        LoginRequestDto loginRequest = new LoginRequestDto("invalidUser", "wrongPassword");

        // Мокваме login метода да хвърля 401 Unauthorized
        when(authService.login(any(LoginRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // Изпращаме POST заявка към /api/auth/login с JSON съдържание
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))) // Преобразуваме DTO-то в JSON
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegister_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Създаваме заявка с невалиден имейл
        RegisterRequestDto invalidRegisterRequest = new RegisterRequestDto(
                "notAnEmail", "pass", "testUser", "John", "Doe",
                "male", LocalDate.of(2000, 1, 1), "0888123456", null);

        // Мокваме register() да хвърли IllegalArgumentException при невалиден имейл
        doThrow(new IllegalArgumentException("Invalid email format"))
                .when(authService).register(any(RegisterRequestDto.class));

        // Изпращаме POST заявка към /register с грешни данни
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegisterRequest))) // Преобразуваме DTO към JSON
                .andExpect(status().isBadRequest()); // Очакваме статус 400 Bad Request
    }

    @Test
    void testRegister_ValidData_ShouldReturnOk() throws Exception {
        // Създаваме валидна заявка за регистрация
        RegisterRequestDto validRegister = new RegisterRequestDto(
                "test@mail.com", "password", "testUser", "John", "Doe",
                "male", LocalDate.of(2000, 1, 1), "0888123456", null);

        // Мокираме register() да не прави нищо (успешна регистрация)
        doNothing().when(authService).register(validRegister);

        // Изпращаме POST заявка към /register с валидни данни
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegister)))
                .andExpect(status().isOk());
    }
}
