package com.example.fooddelivery.config.user;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper mapper;
    private final AddressMapper addressMapper;

    public UserMapper(ModelMapper mapper, AddressMapper addressMapper) {
        this.mapper = mapper;
        this.addressMapper = addressMapper;
    }

    public User mapToUser(RegisterRequestDto registerRequestDto) {
<<<<<<< HEAD

        User user = mapper.map(registerRequestDto, User.class);

        if (user.getGender() == null) {
            user.setGender(Gender.valueOf(registerRequestDto.getGender()));
        }

        return user;
=======
        return mapper.map(registerRequestDto, User.class);
>>>>>>> parent of 25eb82f (update: implement user role management and cookie handling in navigation, sign in, and sign up components; enhance CORS configuration)
    }

    public User mapToUser(LoginRequestDto loginRequestDto) {
        return mapper.map(loginRequestDto, User.class);
    }

    public UserProfileDto mapToUserProfileDto(User user) {
        UserProfileDto dto = mapper.map(user, UserProfileDto.class);

        Set<AddressDto> addresses = user.getAddresses().stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toSet());

        dto.setAddresses(addresses);

        return dto;
    }

    public UserDto mapToUserDto(User user) {
        return mapper.map(user, UserDto.class);
    }

}