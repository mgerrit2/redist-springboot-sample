package com.example.demo.redis.localhost.controllers;


import com.example.demo.redis.localhost.dtos.ProductDto;
import com.example.demo.redis.localhost.mapper.ProductMapper;
import com.example.demo.redis.localhost.services.ProductService;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productSv;


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductDto createProduct(
            @RequestBody
            @Valid
            ProductDto productDto
    ){
        return productSv.createProduct(productDto);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductDto getProduct(@PathVariable long productId){
        return productSv.getProduct(productId);
    }

    @CachePut(value = "productCache", key = "#productDto.id")
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public  ProductDto updateProduct(
            @RequestBody
            @Valid
            ProductDto productDto
    ){
        return productSv.updateProduct(productDto);
    }

    @CacheEvict(value = "productCache", key = "#productId")
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable long productId){
        productSv.deleteById(productId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductDto> getAllProducts() {
        return productSv.getAllProducts();
    }

}
