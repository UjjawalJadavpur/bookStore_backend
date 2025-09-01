package com.example.bookStore.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {

    @NotBlank(message = "Category name is required")
    @Size(max = 20, message = "Category name must not exceed 20 characters")
    private String name;
}
