package com.example.demo.redis.localhost.mapper;

import com.example.demo.redis.localhost.dtos.ProductDto;
import com.example.demo.redis.localhost.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // Private constructor to prevent instantiation
    private ProductMapper() {}

    public  ProductDto toDto(ProductEntity product) {

        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }

    public ProductEntity toEntity(ProductDto dto) {

        if (dto == null) {
            return null;
        }

        ProductEntity product = new ProductEntity();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }

}
