package com.example.demo.redis.localhost.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        Integer version
) {}
