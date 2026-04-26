package com.example.demo.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Product {
    // id, seller_id, title, description, price, stock, created_at

    private Long id;
    private Long sellerId;
    private String title;
    private String description;
    private BigDecimal price;
    private int stock;
    private Instant createdAt;
    private BigDecimal creationFee;
    private boolean creationFeeRefunded;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getCreationFee() {
        return creationFee;
    }

    public void setCreationFee(BigDecimal creationFee) {
        this.creationFee = creationFee;
    }

    public boolean isCreationFeeRefunded() {
        return creationFeeRefunded;
    }

    public void setCreationFeeRefunded(boolean creationFeeRefunded) {
        this.creationFeeRefunded = creationFeeRefunded;
    }
}
