package com.example.fooddelivery.config.user;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserMapper userMapper;
}
