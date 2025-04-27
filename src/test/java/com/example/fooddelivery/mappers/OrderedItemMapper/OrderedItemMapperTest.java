package com.example.fooddelivery.mappers.OrderedItemMapper;

import com.example.fooddelivery.config.ordered_item.OrderedItemMapper;
import com.example.fooddelivery.dto.order.OrderProductDto;
import com.example.fooddelivery.entity.ordered_item.OrderedItem;
import com.example.fooddelivery.entity.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderedItemMapperTest {

    private OrderedItemMapper orderedItemMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        orderedItemMapper = new OrderedItemMapper(modelMapper);
    }

    //mapToProductDto
    //Проверява дали мапването работи правилно
    @Test
    void mapToProductDto_shouldMapFieldsCorrectly() {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(42L);

        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setProduct(product);
        orderedItem.setQuantity(5);

        OrderProductDto dto = orderedItemMapper.mapToProductDto(orderedItem);

        assertEquals(42L, dto.getProductId());
        assertEquals(5, dto.getQuantity());
    }
    //Проверява дали не презаписва стойности, ако вече са зададени
    @Test
    void mapToProductDto_shouldOverwriteFields_whenDtoFieldsAreAlreadySet() {
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(100L);

        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setProduct(product);
        orderedItem.setQuantity(10);

        OrderProductDto resultDto = orderedItemMapper.mapToProductDto(orderedItem);

        assertEquals(100L, resultDto.getProductId());
        assertEquals(10, resultDto.getQuantity());
    }

}
