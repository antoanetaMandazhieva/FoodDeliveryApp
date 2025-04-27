package com.example.fooddelivery.config.user;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.modelmapper.ModelMapper;


import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserMapperTest {
    private ModelMapper modelMapper;
    private AddressMapper addressMapper;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        modelMapper = mock(ModelMapper.class);
        addressMapper = mock(AddressMapper.class);
        userMapper = new UserMapper(modelMapper, addressMapper);
    }

    @Test
    @DisplayName("mapToUser(RegisterRequestDto) should map correctly and handle gender")
    void mapToUser_fromRegisterRequestDto_shouldMapCorrectly() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setGender(Gender.MALE.name());

        User mappedUser = new User();
        when(modelMapper.map(registerRequestDto, User.class)).thenReturn(mappedUser);

        User result = userMapper.mapToUser(registerRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getGender()).isEqualTo(Gender.MALE);
        verify(modelMapper).map(registerRequestDto, User.class);
    }
    @Test
    @DisplayName("mapToUser(LoginRequestDto) should map correctly")
    void mapToUser_fromLoginRequestDto_shouldMapCorrectly() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();

        User mappedUser = new User();
        when(modelMapper.map(loginRequestDto, User.class)).thenReturn(mappedUser);

        User result = userMapper.mapToUser(loginRequestDto);

        assertThat(result).isNotNull();
        verify(modelMapper).map(loginRequestDto, User.class);
    }
    @Test
    @DisplayName("mapToUserProfileDto(User) should map correctly including addresses")
    void mapToUserProfileDto_shouldMapUserAndAddressesCorrectly() {
        // Arrange
        User user = new User();
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Main Street");
        addressDto.setCity("Sofia");

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAddresses(new HashSet<>()); // <- важно
        when(modelMapper.map(user, UserProfileDto.class)).thenReturn(userProfileDto);

        Address address = new Address();
        user.addAddress(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        // Act
        UserProfileDto result = userMapper.mapToUserProfileDto(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAddresses())
                .isNotNull()
                .hasSize(1)
                .allSatisfy(addr -> {
                    assertThat(addr.getStreet()).isEqualTo("Main Street");
                    assertThat(addr.getCity()).isEqualTo("Sofia");
                });


        verify(modelMapper).map(user, UserProfileDto.class);
        verify(addressMapper).toDto(address);
    }
    @Test
    @DisplayName("mapToUserDto(User) should map correctly including role name")
    void mapToUserDto_shouldMapUserAndRoleCorrectly() {
        User user = new User();
        Role role = new Role();
        role.setName("CLIENT");
        user.setRole(role);

        UserDto userDto = new UserDto();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userMapper.mapToUserDto(user);

        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo("CLIENT");

        verify(modelMapper).map(user, UserDto.class);
    }
    @Test
    @DisplayName("mapToUser(RegisterRequestDto) should throw exception if gender is null")
    void mapToUser_fromRegisterRequestDto_shouldThrowException_whenGenderIsNull() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setGender(null);

        User mappedUser = new User();
        when(modelMapper.map(registerRequestDto, User.class)).thenReturn(mappedUser);

        assertThatThrownBy(() -> userMapper.mapToUser(registerRequestDto))
                .isInstanceOf(NullPointerException.class); // или IllegalArgumentException, зависи от твоята имплементация
    }
    /*
    @Test
    @DisplayName("mapToUserDto(User) should handle null role")
    void mapToUserDto_shouldHandleNullRole() {
        User user = new User();
        user.setRole(null); // <- важно

        UserDto userDto = new UserDto();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userMapper.mapToUserDto(user);

        assertThat(result).isNotNull();
        assertThat(result.getRole()).isNull(); // role няма да бъде сетнат
        verify(modelMapper).map(user, UserDto.class);
    }*/

}

