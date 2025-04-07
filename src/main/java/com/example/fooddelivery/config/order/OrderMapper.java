package com.example.fooddelivery.config.order;

import com.example.fooddelivery.config.common.Mapper;
import com.example.fooddelivery.config.product.ProductMapper;
import com.example.fooddelivery.dto.order.OrderCreateDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.entity.Order;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper {

    private static final ModelMapper mapper = Mapper.getInstance();

    public static OrderDto mapFromOrderToDto(Order order) {
        OrderDto orderDto = mapper.map(order, OrderDto.class);

        orderDto.setClientName(order.getClient().getName());
        orderDto.setRestaurantName(order.getRestaurant().getName());
        orderDto.setSupplierName(order.getSupplier() != null ? order.getSupplier().getUsername() : null);

        Set<ProductDto> productDtos = order.getProducts().stream()
                .map(ProductMapper::mapToProductDto)
                .collect(Collectors.toSet());

        orderDto.setProducts(productDtos);
        orderDto.setOrderStatus(order.getOrderStatus().name());

        return orderDto;
    }

    public static Order mapFromOrderCreateDto(OrderCreateDto orderCreateDto) {
        Order order = new Order();
        // TODO -> Ще се добавят продуктите и ресторанта по ID в самия OrderServiceImpl

        return order;
    }
}