package com.example.bookStore.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Long categoryId;   // only pass category id from frontend
    private String imageUrl;
    private String status;     // could be "IN_STOCK" or "OUT_OF_STOCK"
}
