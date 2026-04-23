package com.example.demo.model;


// id, buyer_id, seller_id, product_id, order_price, destination, status (ORDERED/SHIPPED/READY_TO_CLAIM/COMPLETED), created_at


import java.math.BigDecimal;
import java.time.Instant;

public class Order {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long productId;
    private BigDecimal orderPrice;
    private int amount;
    private String destination;
    private String status;
    private Instant createdAt;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {}
}
