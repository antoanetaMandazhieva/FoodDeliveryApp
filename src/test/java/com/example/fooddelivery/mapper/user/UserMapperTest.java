package com.example.fooddelivery.mapper.user;

import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.auth.LoginRequestDto;
import com.example.fooddelivery.dto.auth.RegisterRequestDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.Gender;
import com.example.fooddelivery.mapper.address.AddressMapper;
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

        // Мокваме резултата от мапването
        User mappedUser = new User();
        when(modelMapper.map(registerRequestDto, User.class)).thenReturn(mappedUser);

        // Извикваме метода за мапване
        User result = userMapper.mapToUser(registerRequestDto);

        // Проверяваме дали резултатът е валиден и дали gender е коректно мапнато
        assertThat(result).isNotNull();
        assertThat(result.getGender()).isEqualTo(Gender.MALE);
        verify(modelMapper).map(registerRequestDto, User.class);
    }
    @Test
    @DisplayName("mapToUser(LoginRequestDto) should map correctly")
    void mapToUser_fromLoginRequestDto_shouldMapCorrectly() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();

        // Мокваме резултата от мапването
        User mappedUser = new User();
        when(modelMapper.map(loginRequestDto, User.class)).thenReturn(mappedUser);

        // Извикваме метода за мапване
        User result = userMapper.mapToUser(loginRequestDto);

        // Проверяваме дали резултатът не е null
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

        // Мокваме резултата от мапването
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAddresses(new HashSet<>()); // Инициализираме адресите като празен сет
        when(modelMapper.map(user, UserProfileDto.class)).thenReturn(userProfileDto);

        // Инициализираме адресите като празен сет
        Address address = new Address();
        user.addAddress(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);

        // Извикваме метода за мапване
        UserProfileDto result = userMapper.mapToUserProfileDto(user);

        // Проверяваме дали адресите са мапнати правилно
        assertThat(result).isNotNull();
        assertThat(result.getAddresses())
                .isNotNull()
                .hasSize(1) // Проверка дали има точно 1 адрес
                .allSatisfy(addr -> { // Проверка дали всеки адрес е коректен
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

        // Мокваме резултата от мапването
        UserDto userDto = new UserDto();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Извикваме метода за мапване
        UserDto result = userMapper.mapToUserDto(user);

        // Проверяваме дали ролята е правилно мапната
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo("CLIENT");  // Проверка дали ролята е мапната правилно

        // Проверка дали ModelMapper е извикан
        verify(modelMapper).map(user, UserDto.class);
    }
    @Test
    @DisplayName("mapToUser(RegisterRequestDto) should throw exception if gender is null")
    void mapToUser_fromRegisterRequestDto_shouldThrowException_whenGenderIsNull() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setGender(null);

        // Мокваме резултата от мапването
        User mappedUser = new User();
        when(modelMapper.map(registerRequestDto, User.class)).thenReturn(mappedUser);

        // Проверка дали ще бъде хвърлена NullPointerException (или IllegalArgumentException, в зависимост от имплементацията)
        assertThatThrownBy(() -> userMapper.mapToUser(registerRequestDto))
                .isInstanceOf(NullPointerException.class);
    }
}

