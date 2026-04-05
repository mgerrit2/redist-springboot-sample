package com.example.demo.redis.localhost.controllers;


import com.example.demo.redis.localhost.dtos.ProductDto;
import com.example.demo.redis.localhost.services.ProductService;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public  ProductDto updateProduct(
            @RequestBody
            @Valid
            ProductDto productDto
    ){
        return productSv.updateProduct(productDto);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable long productId){
        productSv.deleteById(productId);
    }

}
