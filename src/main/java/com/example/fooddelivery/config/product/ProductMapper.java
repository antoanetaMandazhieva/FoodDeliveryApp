package com.example.fooddelivery.config.product;

import com.example.fooddelivery.dto.product.ProductDto;
import com.example.fooddelivery.entity.cuisine.Cuisine;
import com.example.fooddelivery.entity.product.Product;
import com.example.fooddelivery.enums.Category;
import com.example.fooddelivery.repository.CuisineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final ModelMapper mapper;
    private final CuisineRepository cuisineRepository;

    public ProductMapper(ModelMapper mapper, CuisineRepository cuisineRepository) {
        this.mapper = mapper;
        this.cuisineRepository = cuisineRepository;
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
        Product product = new Product();

        product.setName(product.getName());
        product.setPrice(product.getPrice());
        product.setDescription(product.getDescription());
        product.setCategory(Category.valueOf(productDto.getCategory()));

        Cuisine cuisine = cuisineRepository.findByName(productDto.getCuisineName())
                        .orElseThrow(() -> new EntityNotFoundException("Cuisine not found"));

        product.setCuisine(cuisine);

        return product;
    }

}