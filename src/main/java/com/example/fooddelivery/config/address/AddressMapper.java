package com.example.fooddelivery.config.address;

import com.example.fooddelivery.config.common.Mapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.entity.Address;
import org.modelmapper.ModelMapper;

public class AddressMapper {

    private static final ModelMapper mapper = Mapper.getInstance();

    public static Address mapToAddress(AddressDto addressDto) {
        return mapper.map(addressDto, Address.class);
    }

    public static AddressDto toDto(Address address) {
        return mapper.map(address, AddressDto.class);
    }
}