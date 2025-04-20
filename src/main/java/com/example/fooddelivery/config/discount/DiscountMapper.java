package com.example.fooddelivery.config.discount;

import com.example.fooddelivery.dto.discount.DiscountDto;
import com.example.fooddelivery.entity.discount.Discount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {

    private final ModelMapper mapper;

    public DiscountMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public DiscountDto toDiscountDto(Discount discount) {
        DiscountDto dto = new DiscountDto();

        dto.setUserId(discount.getUser().getId());
        dto.setDiscountAmount(discount.getDiscountAmount());
        dto.setRole(discount.getUser().getRole().getName());

        return dto;
    }
}