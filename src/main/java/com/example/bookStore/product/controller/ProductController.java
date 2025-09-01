package com.example.bookStore.product.controller;

import com.example.bookStore.product.dto.ProductRequestDto;
import com.example.bookStore.product.dto.ProductResponseDto;
import com.example.bookStore.product.model.Product;
import com.example.bookStore.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto dto) {
        return productService.saveProduct(dto);
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

}
