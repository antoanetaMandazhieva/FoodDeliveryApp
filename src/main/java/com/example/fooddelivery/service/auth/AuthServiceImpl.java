package com.example.fooddelivery.service.auth;

import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.LoginResponseDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.exception.auth.InvalidCredentialsException;
import com.example.fooddelivery.exception.role.RoleNotFoundException;
import com.example.fooddelivery.exception.auth.UserAlreadyExistsException;
import com.example.fooddelivery.repository.RoleRepository;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fooddelivery.util.Messages.SUCCESSFUL_LOGIN_MESSAGE;
import static com.example.fooddelivery.util.SystemErrors.Auth.*;
import static com.example.fooddelivery.util.SystemErrors.Role.NO_CLIENT_ROLE;

@Service
public class AuthServiceImpl implements AuthService {

    private final static String CLIENT_ROLE = "CLIENT";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, AddressMapper addressMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }

    @Transactional
    @Override
    public void register(RegisterRequestDto registerRequestDto) {
        validateUserUniqueness(registerRequestDto);

        User user = buildUserFromDto(registerRequestDto);

        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_USERNAME));

        if (!user.checkPassword(loginRequestDto.getPassword())) {
            throw new InvalidCredentialsException(INVALID_PASSWORD);
        }

        return new LoginResponseDto(user.getId(), user.getRole().getName(), SUCCESSFUL_LOGIN_MESSAGE);
    }


    private void validateUserUniqueness(RegisterRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(EMAIL_ALREADY_TAKEN);
        }

        if (userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistsException(PHONE_NUMBER_ALREADY_TAKEN);
        }
    }

    private User buildUserFromDto(RegisterRequestDto registerRequestDto) {
        User user = userMapper.mapToUser(registerRequestDto);


        Role clientRole = roleRepository.findByName(CLIENT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException(NO_CLIENT_ROLE));

        clientRole.addUser(user);

        AddressDto addressDto = registerRequestDto.getAddress();
        Address address = addressMapper.mapToAddress(addressDto);

        // Всеки клиент има един адрес при регистрация
        user.addAddress(address);

        return user;
    }
}