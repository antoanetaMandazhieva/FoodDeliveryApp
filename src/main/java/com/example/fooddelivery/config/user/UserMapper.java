package com.example.fooddelivery.config.user;

import com.example.fooddelivery.config.common.Mapper;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private static final ModelMapper mapper = Mapper.getInstance();

    public static User mapToUser(RegisterRequestDto registerRequestDto) {
        return mapper.map(registerRequestDto, User.class);
    }

    public static User mapToUser(LoginRequestDto loginRequestDto) {
        return mapper.map(loginRequestDto, User.class);
    }

    public static UserProfileDto mapToUserProfileDto(User user) {
        return mapper.map(user, UserProfileDto.class);
    }

    public static UserDto mapToUserDto(User user) {
        return mapper.map(user, UserDto.class);
    }

}