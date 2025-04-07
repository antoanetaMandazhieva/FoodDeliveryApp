package com.example.fooddelivery.config.product;

import com.example.fooddelivery.config.common.Mapper;
import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.entity.Product;
import org.modelmapper.ModelMapper;

public class ProductMapper {

    private static final ModelMapper mapper = Mapper.getInstance();

    public static ProductDto mapToProductDto(Product product) {
        ProductDto productDto = mapper.map(product, ProductDto.class);

        if (product.getCuisine() != null) {
            productDto.setCuisineName(product.getCuisine().getName());
        }

        if (product.getRestaurant() != null) {
            productDto.setRestaurantName(product.getRestaurant().getName());
        }

        return productDto;
    }

    public static Product mapToProduct(ProductDto productDto) {
        return mapper.map(productDto, Product.class);
    }

}