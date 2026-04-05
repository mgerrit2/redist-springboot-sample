package com.example.demo.redis.localhost;


import com.example.demo.redis.localhost.dtos.ProductDto;
import com.example.demo.redis.localhost.entity.ProductEntity;
import com.example.demo.redis.localhost.mapper.ProductMapper;
import com.example.demo.redis.localhost.repositorys.ProductRepository;
import com.example.demo.redis.localhost.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;


import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Assumes the application is set up with Redis config and caching enabled
@SpringBootTest
public class ProductServiceIntegrationTest {

    private static final String CACHE_NAME = "PRODUCT_CACHE";
    private static final long PRODUCT_ID = 100L;

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    // Use MockBean to isolate the service from real database and mapping logic
    @MockBean
    private ProductRepository productRs;

    @MockBean
    private ProductMapper productMap;

    private ProductDto validDto;
    private ProductEntity validEntity;

    @BeforeEach
    void setUp() {
        // Clear the cache before each test to ensure isolation
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.clear();
        }

        // Setup common test data
        validDto = ProductDto.builder()
                .id(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .build();

        validEntity = new ProductEntity();
        validEntity.setId(PRODUCT_ID);
        validEntity.setName("Test Product");
        validEntity.setPrice(new BigDecimal("99.99"));

        // Configure Mocks for common success paths
        when(productMap.toEntity(any(ProductDto.class))).thenReturn(validEntity);
        when(productMap.toDto(any(ProductEntity.class))).thenReturn(validDto);
        when(productRs.save(any(ProductEntity.class))).thenReturn(validEntity);
        when(productRs.findById(PRODUCT_ID)).thenReturn(Optional.of(validEntity));
    }

    private ProductDto getCacheValue(long id) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            return cache.get(id, ProductDto.class);
        }
        return null;
    }

    // =========================================================================
    // CRUD and Caching Tests
    // =========================================================================

    @Test
    void testCreateProduct_shouldSaveAndCache() {
        // 1. Act: Create the product
        ProductDto createdDto = productService.createProduct(validDto);

        // 2. Assert Service Logic: Verify repository save was called
        verify(productRs, times(1)).save(any(ProductEntity.class));
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isEqualTo(PRODUCT_ID);

        // 3. Assert Caching (@CachePut): Verify item is in the cache
        ProductDto cachedDto = getCacheValue(PRODUCT_ID);
        assertThat(cachedDto).isEqualTo(createdDto);
    }

    @Test
    void testGetProduct_shouldCacheFirstCallButNotSecond() {
        // 1. First Call: Should hit the repository and populate the cache
        ProductDto firstCallDto = productService.getProduct(PRODUCT_ID);
        verify(productRs, times(1)).findById(PRODUCT_ID);
        assertThat(firstCallDto).isNotNull();

        // 2. Second Call: Should hit the cache, NOT the repository
        ProductDto secondCallDto = productService.getProduct(PRODUCT_ID);
        verify(productRs, times(1)).findById(PRODUCT_ID); // Count remains 1
        assertThat(secondCallDto).isEqualTo(firstCallDto);

        // 3. Assert Caching: Verify item is in the cache
        ProductDto cachedDto = getCacheValue(PRODUCT_ID);
        assertThat(cachedDto).isEqualTo(firstCallDto);
    }

    @Test
    void testUpdateProduct_shouldUpdateDbAndRefreshCache() {
        // Setup cache: Ensure item exists in cache before update
        cacheManager.getCache(CACHE_NAME).put(PRODUCT_ID, validDto);

        // Prepare updated DTO
        ProductDto updatedDtoInput = ProductDto.builder()
                .id(PRODUCT_ID)
                .name("Updated Name")
                .price(new BigDecimal("199.99"))
                .build();

        // 1. Act: Update the product
        ProductDto updatedDto = productService.updateProduct(updatedDtoInput);

        // 2. Assert Service Logic: Verify findById and save were called
        verify(productRs, times(1)).findById(PRODUCT_ID);
        verify(productRs, times(1)).save(any(ProductEntity.class));
        assertThat(updatedDto.getName()).isEqualTo("Updated Name");

        // 3. Assert Caching (@CachePut): Verify cache value is refreshed
        ProductDto cachedDto = getCacheValue(PRODUCT_ID);
        assertThat(cachedDto.getName()).isEqualTo("Updated Name");
        assertThat(cachedDto).isEqualTo(updatedDto);
    }

    // NOTE on @Valid: Since @Valid on a service method parameter is not standard Spring
    // practice for validation (it typically works on Controllers), and validation
    // is usually handled via an Exception Handler in the Controller layer, we skip
    // detailed validation tests here and focus on caching/service logic.

    @Test
    void testDeleteById_shouldDeleteFromDbAndEvictCache() {
        // Setup cache: Put item in cache manually
        cacheManager.getCache(CACHE_NAME).put(PRODUCT_ID, validDto);
        assertThat(getCacheValue(PRODUCT_ID)).isNotNull();

        // 1. Act: Delete the product
        productService.deleteById(PRODUCT_ID);

        // 2. Assert Service Logic: Verify delete was called
        verify(productRs, times(1)).deleteById(PRODUCT_ID);

        // 3. Assert Caching (@CacheEvict): Verify item is evicted from cache
        assertThat(getCacheValue(PRODUCT_ID)).isNull();
    }

    @Test
    void testGetProduct_withNotFoundId_shouldThrowException() {
        // Configure mock to return empty optional for a different ID
        long nonExistentId = 999L;
        when(productRs.findById(nonExistentId)).thenReturn(Optional.empty());

        // Assert: Throws the correct exception
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProduct(nonExistentId);
        });

        // Assert: Ensure nothing was cached
        assertThat(getCacheValue(nonExistentId)).isNull();
    }
}
