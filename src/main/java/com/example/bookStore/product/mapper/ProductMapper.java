package com.example.bookStore.product.mapper;

import com.example.bookStore.category.model.Category;
import com.example.bookStore.product.dto.ProductRequestDto;
import com.example.bookStore.product.dto.ProductResponseDto;
import com.example.bookStore.product.model.Product;
import com.example.bookStore.product.model.ProductStatus;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto, Category category) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .category(category)
                .imageUrl(dto.getImageUrl())
                .status(ProductStatus.valueOf(dto.getStatus()))
                .build();
    }

    public static ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryName(product.getCategory().getName())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus().name())
                .build();
    }
}
