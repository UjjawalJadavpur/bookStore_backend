package com.example.bookStore.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String categoryName; // show category name instead of object
    private String imageUrl;
    private String status;
}
