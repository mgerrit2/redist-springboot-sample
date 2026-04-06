package com.example.demo.redis.localhost.controllers;


import com.example.demo.redis.localhost.dtos.ProductDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/debug")
public class DebuggingProduct {

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<ProductDto> getProduct(){
        return ResponseEntity.ok(ProductDto.builder()
                .id(1L)
                .name("name")
                .price(new BigDecimal(100))
                .build());

    }


}
