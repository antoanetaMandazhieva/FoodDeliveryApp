package com.example.fooddelivery.config.ordered_item;

import com.example.fooddelivery.dto.order.OrderProductDto;
import com.example.fooddelivery.entity.ordered_item.OrderedItem;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderedItemMapper {

    private final ModelMapper mapper;

    public OrderedItemMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }


    public OrderProductDto mapToProductDto(OrderedItem orderedItem) {
        OrderProductDto orderProductDto = mapper.map(orderedItem, OrderProductDto.class);

        if (orderProductDto.getProductId() == null) {
            orderProductDto.setProductId(orderedItem.getProduct().getId());
        }

        if (orderProductDto.getQuantity() == 0) {
            orderProductDto.setQuantity(orderedItem.getQuantity());
        }

        return orderProductDto;
    }
}