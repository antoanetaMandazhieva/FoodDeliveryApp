package com.example.fooddelivery.config.order;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.ordered_item.OrderedItemMapper;
import com.example.fooddelivery.config.product.ProductMapper;
import com.example.fooddelivery.dto.address.AddressDto;
import com.example.fooddelivery.dto.order.OrderDto;
import com.example.fooddelivery.dto.order.OrderProductDto;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.order.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final ModelMapper mapper;
    private final AddressMapper addressMapper;
    private final OrderedItemMapper orderedItemMapper;

    public OrderMapper(ModelMapper mapper, AddressMapper addressMapper, OrderedItemMapper orderedItemMapper) {
        this.mapper = mapper;
        this.addressMapper = addressMapper;
        this.orderedItemMapper = orderedItemMapper;
    }

    public OrderDto mapFromOrderToDto(Order order) {
        OrderDto orderDto = mapper.map(order, OrderDto.class);

        orderDto.setClientName(order.getClient().getName());
        orderDto.setRestaurantName(order.getRestaurant().getName());
        orderDto.setSupplierName(order.getSupplier() != null ? order.getSupplier().getUsername() : null);

        List<OrderProductDto> productDtos = order.getOrderedItems().stream()
                .sorted(Comparator.comparing(orderedItem -> orderedItem.getId().getProductId()))
                .map(orderedItemMapper::mapToProductDto)
                .collect(Collectors.toList());

        orderDto.setProducts(productDtos);
        orderDto.setOrderStatus(order.getOrderStatus().name());


        if (order.getDiscount() != null) {
            Discount discount = order.getDiscount();
            BigDecimal discountAmount = discount.getDiscountAmount();

            if (discountAmount.equals(BigDecimal.valueOf(0.1))) {
                orderDto.setDiscount("10%");
            } else if (discountAmount.equals(BigDecimal.valueOf(0.2))) {
                orderDto.setDiscount("20%");
            } else {
                orderDto.setDiscount("30%");
            }
        }

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

        if (order.getDiscount() != null) {
            BigDecimal discountAmount = order.getDiscount().getDiscountAmount();

            // BigDecimal.equals -> Сравянва и scale -> 0.1 != 0.10

            if (discountAmount.compareTo(BigDecimal.valueOf(0.1)) == 0) {
                orderResponseDto.setDiscount("10%");
            } else if (discountAmount.compareTo(BigDecimal.valueOf(0.20)) == 0) {
                orderResponseDto.setDiscount("20%");
            } else if (discountAmount.compareTo(BigDecimal.valueOf(0.30)) == 0) {
                orderResponseDto.setDiscount("30%");
            }
        }

        return orderResponseDto;
    }
}