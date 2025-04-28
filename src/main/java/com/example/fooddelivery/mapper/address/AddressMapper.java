package com.example.fooddelivery.mapper.address;

import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.entity.address.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final ModelMapper mapper;

    public AddressMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }


    public Address mapToAddress(AddressDto addressDto) {
        return mapper.map(addressDto, Address.class);
    }

    public AddressDto toDto(Address address) {
        return mapper.map(address, AddressDto.class);
    }
}