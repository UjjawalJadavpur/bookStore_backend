package com.example.bookStore.category.controller;

import com.example.bookStore.category.dto.CategoryRequestDto;
import com.example.bookStore.category.dto.CategoryResponseDto;
import com.example.bookStore.category.model.Category;
import com.example.bookStore.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto createCategory(@RequestBody CategoryRequestDto categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .build();
        Category savedCategory = categoryService.saveCategory(category);
        return new CategoryResponseDto(savedCategory.getCategoryId(), savedCategory.getName());
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories()
                .stream()
                .map(cat -> new CategoryResponseDto(cat.getCategoryId(), cat.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return new CategoryResponseDto(category.getCategoryId(), category.getName());
    }
}

