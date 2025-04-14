package com.example.fooddelivery.config.order;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.product.ProductMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final ModelMapper mapper;
    private final AddressMapper addressMapper;
    private final ProductMapper productMapper;

    public OrderMapper(ModelMapper mapper, AddressMapper addressMapper, ProductMapper productMapper) {
        this.mapper = mapper;
        this.addressMapper = addressMapper;
        this.productMapper = productMapper;
    }

    public OrderDto mapFromOrderToDto(Order order) {
        OrderDto orderDto = mapper.map(order, OrderDto.class);

        orderDto.setClientName(order.getClient().getName());
        orderDto.setRestaurantName(order.getRestaurant().getName());
        orderDto.setSupplierName(order.getSupplier() != null ? order.getSupplier().getUsername() : null);

        Set<ProductDto> productDtos = order.getProducts().stream()
                .map(productMapper::mapToProductDto)
                .collect(Collectors.toSet());

        orderDto.setProducts(productDtos);
        orderDto.setOrderStatus(order.getOrderStatus().name());

        return orderDto;
    }

    public OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto orderResponseDto = mapper.map(order, OrderResponseDto.class);

        if (order.getRestaurant() != null) {
            orderResponseDto.setRestaurantName(order.getRestaurant().getName());
        }

        if (orderResponseDto.getOrderStatus() == null) {
            orderResponseDto.setOrderStatus(order.getOrderStatus().name());
        }

        if (orderResponseDto.getClientAddress() == null) {
            AddressDto addressDto = order.getClient().getAddresses().stream()
                    .findFirst()
                    .map(addressMapper::toDto)
                    .orElseThrow(() -> new IllegalStateException("No address for that user"));

            String clientAddress = String.format("Country: %s, City: %s, Street: %s", addressDto.getCountry(),
                    addressDto.getCity(), addressDto.getStreet());

            orderResponseDto.setClientAddress(clientAddress);
        }

        if (orderResponseDto.getClientPhone() == null) {
            orderResponseDto.setClientPhone(order.getClient().getPhoneNumber());
        }

        return orderResponseDto;
    }
}