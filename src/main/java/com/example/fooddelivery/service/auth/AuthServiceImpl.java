package com.example.fooddelivery.service.auth;

import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.LoginResponseDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.entity.Address;
import com.example.fooddelivery.entity.Role;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.repository.RoleRepository;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void register(RegisterRequestDto registerRequestDto) {
        User user = configureUser(registerRequestDto);

        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username."));

        if (!user.checkPassword(loginRequestDto.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        return new LoginResponseDto(user.getUsername(), user.getRole().getName());
    }

    private User configureUser(RegisterRequestDto registerRequestDto) {
        User user = UserMapper.mapToUser(registerRequestDto);

        if (isUserExistsInDb(user.getUsername())) {
            throw new RuntimeException("User with this name already exists.");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("There is no CLIENT role!"));

        clientRole.addUser(user);

        AddressDto addressDto = registerRequestDto.getAddress();
        Address address = AddressMapper.mapToAddress(addressDto);

        user.addAddress(address);

        return user;
    }

    private boolean isUserExistsInDb(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}