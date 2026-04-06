package com.example.demo.redis.localhost.services;


import com.example.demo.redis.localhost.dtos.ProductDto;
import com.example.demo.redis.localhost.mapper.ProductMapper;
import com.example.demo.redis.localhost.repositorys.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRs;
    private final ProductMapper productMap;

    @CachePut(value = "PRODUCT_CACHE", key="#result.id()")
    public ProductDto createProduct(ProductDto productDto) {
        return productMap.toDto(productRs.save(productMap.toEntity(productDto)));
    }

    @Cacheable(value = "PRODUCT_CACHE", key="#productId")
    public ProductDto getProduct(long productId) {
      var product =  productRs.findById(productId).
              orElseThrow(()-> new IllegalArgumentException("Cannot find product with id "+ productId));

      return ProductDto.builder()
              .id(product.getId())
              .name(product.getName())
              .price(product.getPrice())
              .build();
    }

    @CachePut(value = "PRODUCT_CACHE", key="#result.id()")
    public ProductDto updateProduct(@Valid ProductDto productDto) {
        long productId = productDto.id();
        var productEntity = productRs.findById(productId).orElseThrow(()
                -> new IllegalArgumentException("Cannot find product with id " + productId));

        productEntity.setName(productDto.name());
        productEntity.setPrice(productDto.price());

        var updateProduct = productRs.save(productEntity);

        return ProductDto.builder()
                .id(updateProduct.getId())
                .name(updateProduct.getName())
                .price(updateProduct.getPrice())
                .build();

    }

    @CacheEvict(value = "PRODUCT_CACHE", key="#productId")
    public void deleteById(long productId) {
        productRs.deleteById(productId);
    }


    public List<ProductDto> getAllProducts() {
        return productMap.toDtoList(productRs.findAll());
    }
}
