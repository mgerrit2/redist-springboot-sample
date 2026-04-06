package com.example.demo.redis.localhost.mapper;

import com.example.demo.redis.localhost.dtos.ProductDto;
import com.example.demo.redis.localhost.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR // Goede praktijk: geeft fout bij vergeten velden
)
public interface ProductMapper {

    // MapStruct genereert de implementatie hiervan automatisch
    @Mapping(target = "version", ignore = true)
    ProductDto toDto(ProductEntity product);

    List<ProductDto> toDtoList(List<ProductEntity> products);

    // MapStruct genereert ook dit automatisch, geen handmatige code nodig!
    @Mapping(target = "version", source = "version")
    ProductEntity toEntity(ProductDto dto);

}
