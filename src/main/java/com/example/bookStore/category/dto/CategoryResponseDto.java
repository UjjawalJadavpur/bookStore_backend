package com.example.bookStore.category.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private Long categoryId;
    private String name;
}
