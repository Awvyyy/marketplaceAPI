package com.example.demo.service;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    public ProductService(ProductMapper productMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    public List<Product> getAllProducts(){
        return productMapper.findAll();
    }

    public Product getProductById(Long id){
        Product product = productMapper.findById(id);
        if (product == null){
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    @Transactional
    public Product createProduct(Long sellerId, String title, String description, BigDecimal price, int stock){
        if (sellerId == null) {
            throw new RuntimeException("Seller id cannot be null");
        }

        if (title == null || title.isBlank()) {
            throw new RuntimeException("Title cannot be empty");
        }

        if (description == null || description.isBlank()) {
            throw new RuntimeException("Description cannot be empty");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Price cannot be null or 0");
        }

        if (stock < 0){
            throw new RuntimeException("Stock cannot be less than 0");
        }

        User user = userMapper.getUserById(sellerId);
        if (user == null){
            throw new RuntimeException("User not found");
        }

        if (user.getSales() < 2) {
            if (user.getBalance().compareTo(price) < 0) {
                throw new RuntimeException("Insufficient funds");
            }

            BigDecimal newBalance = user.getBalance().subtract(price);
            int updatedRows = userMapper.updateBalance(sellerId, newBalance);
            if (updatedRows != 1) {
                throw new RuntimeException("Failed to update user balance");
            }
        }

        Product product = new Product();
        product.setSellerId(sellerId);
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);

        int insertedRows = productMapper.createProduct(product);
        if (insertedRows != 1) {
            throw new RuntimeException("Failed to create product");
        }

        return productMapper.findById(product.getId());
    }

    public List<Product> getAllProductsByUserId(Long sellerId){
        return productMapper.findAllProductsBySellerId(sellerId);
    }
}
