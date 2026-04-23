package com.example.demo.controller;

import com.example.demo.dto.request.CreateProductRequestDto;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private ProductResponseDto toResponse(Product product) {
        return new ProductResponseDto(product.getId(), product.getSellerId(), product.getTitle(),
        product.getDescription(), product.getPrice(), product.getStock(), product.getCreatedAt()
        );
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return toResponse(product);
    }

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody CreateProductRequestDto request) {
        Product product = productService.createProduct(request.getSellerId(), request.getTitle(),
        request.getDescription(), request.getPrice(), request.getStock());

        return toResponse(product);
    }
}
