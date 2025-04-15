package com.example.fooddelivery.controller.auth;


import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.LoginResponseDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowCredentials = "true"
)
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        authService.register(registerRequestDto);
        System.out.println(registerRequestDto);
        System.out.println("Test: " + registerRequestDto.getUsername());

        LoginRequestDto loginRequestDto = new LoginRequestDto(
                registerRequestDto.getUsername(),
                registerRequestDto.getPassword());

        LoginResponseDto loginResponse = authService.login(loginRequestDto);
        System.out.println("Test: " + loginResponse);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto);
        loginResponse.setMessage("Login Successful");
        System.out.println("Test: " + loginResponse);
        return ResponseEntity.ok(loginResponse);
    }
}