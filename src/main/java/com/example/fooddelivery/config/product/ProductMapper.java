package com.example.fooddelivery.config.product;

import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final ModelMapper mapper;

    public ProductMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProductDto mapToProductDto(Product product) {
        ProductDto productDto = mapper.map(product, ProductDto.class);

        if (product.getCuisine() != null) {
            productDto.setCuisineName(product.getCuisine().getName());
        }

        if (product.getRestaurant() != null) {
            productDto.setRestaurantName(product.getRestaurant().getName());
        }

        return productDto;
    }

    public Product mapToProduct(ProductDto productDto) {
        return mapper.map(productDto, Product.class);
    }

}