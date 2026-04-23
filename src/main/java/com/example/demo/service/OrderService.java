package com.example.demo.service;

import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    public OrderService(OrderMapper orderMapper, ProductMapper productMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public Order createOrder(Long buyerId, Long productId, String destination, int amount) {
        if (buyerId == null) {
            throw new RuntimeException("buyerId cannot be null");
        }

        if (productId == null) {
            throw new RuntimeException("productId cannot be null");
        }

        if (destination == null || destination.isBlank()) {
            throw new RuntimeException("Destination cannot be null or empty");
        }

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        User buyer = userMapper.getUserById(buyerId);
        if (buyer == null) {
            throw new RuntimeException("Buyer not found");
        }

        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (buyer.getId().equals(product.getSellerId())) {
            throw new RuntimeException("Cannot buy your own product");
        }

        if (product.getStock() < amount) {
            throw new RuntimeException("Not enough product stock");
        }

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(amount));

        if (buyer.getBalance().compareTo(totalPrice) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal newBuyerBalance = buyer.getBalance().subtract(totalPrice);
        int updatedBuyerRows = userMapper.updateBalance(buyerId, newBuyerBalance);
        if (updatedBuyerRows != 1) {
            throw new RuntimeException("Failed to update buyer balance");
        }

        int newStock = product.getStock() - amount;
        int updatedStockRows = productMapper.updateStock(productId, newStock);
        if (updatedStockRows != 1) {
            throw new RuntimeException("Failed to update product stock");
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setSellerId(product.getSellerId());
        order.setProductId(productId);
        order.setOrderPrice(totalPrice);
        order.setDestination(destination);
        order.setAmount(amount);

        int insertedRows = orderMapper.createOrder(order);
        if (insertedRows != 1) {
            throw new RuntimeException("Failed to create order");
        }

        return orderMapper.findById(order.getId());
    }

    public Order getOrderById(Long id) {
        if (id == null) {
            throw new RuntimeException("Order id cannot be null");
        }

        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        return order;
    }

    @Transactional
    public Order updateStatus(Long id, String newStatus) {
        if (id == null) {
            throw new RuntimeException("Order id cannot be null");
        }

        if (newStatus == null || newStatus.isBlank()) {
            throw new RuntimeException("Status cannot be null or empty");
        }

        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        String currentStatus = order.getStatus();

        boolean validTransition =
                ("ORDERED".equals(currentStatus) && "SHIPPED".equals(newStatus)) ||
                        ("SHIPPED".equals(currentStatus) && "READY_TO_CLAIM".equals(newStatus)) ||
                        ("READY_TO_CLAIM".equals(currentStatus) && "COMPLETED".equals(newStatus));

        if (!validTransition) {
            throw new RuntimeException("Invalid status transition");
        }

        int updatedRows = orderMapper.updateStatus(id, newStatus);
        if (updatedRows != 1) {
            throw new RuntimeException("Failed to update order status");
        }

        if ("COMPLETED".equals(newStatus)) {
            User seller = userMapper.getUserById(order.getSellerId());
            if (seller == null) {
                throw new RuntimeException("Seller not found");
            }

            BigDecimal newSellerBalance = seller.getBalance().add(order.getOrderPrice());
            int updatedSellerRows = userMapper.updateBalance(seller.getId(), newSellerBalance);
            if (updatedSellerRows != 1) {
                throw new RuntimeException("Failed to update seller balance");
            }

            int newSellerSales = seller.getSales() + 1;
            int updatedSellerSalesRows = userMapper.updateSales(seller.getId(), newSellerSales);
            if (updatedSellerSalesRows != 1) {
                throw new RuntimeException("Failed to update seller sales");
            }
        }

        return orderMapper.findById(id);
    }
}