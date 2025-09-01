package com.example.bookStore.product.service;

import com.example.bookStore.category.model.Category;
import com.example.bookStore.category.repository.CategoryRepository;
import com.example.bookStore.product.dto.ProductRequestDto;
import com.example.bookStore.product.dto.ProductResponseDto;
import com.example.bookStore.product.mapper.ProductMapper;
import com.example.bookStore.product.model.Product;
import com.example.bookStore.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponseDto saveProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = ProductMapper.toEntity(dto, category);
        Product saved = productRepository.save(product);

        return ProductMapper.toDto(saved);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDto)
                .orElse(null);
    }
}
