package com.example.fooddelivery.controller.auth;

import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.LoginResponseDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Tested!
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        authService.register(registerRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto(
                registerRequestDto.getUsername(),
                registerRequestDto.getPassword());

        LoginResponseDto loginResponse = authService.login(loginRequestDto);

        return ResponseEntity.ok(loginResponse);
    }

    // Tested!
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto);

        return ResponseEntity.ok(loginResponse);
    }
}