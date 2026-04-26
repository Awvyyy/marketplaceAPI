package com.example.demo.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_success() {
        User buyer = new User();
        buyer.setId(1L);
        buyer.setBalance(new BigDecimal("1000.00"));

        Product product = new Product();
        product.setId(2L);
        product.setSellerId(3L);
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(5);

        Order savedOrder = new Order();
        savedOrder.setId(10L);
        savedOrder.setBuyerId(1L);
        savedOrder.setSellerId(3L);
        savedOrder.setProductId(2L);
        savedOrder.setOrderPrice(new BigDecimal("200.00"));
        savedOrder.setAmount(2);
        savedOrder.setDestination("EE");
        savedOrder.setStatus("ORDERED");

        when(userMapper.getUserById(1L)).thenReturn(buyer);
        when(productMapper.findById(2L)).thenReturn(product);
        when(userMapper.updateBalance(eq(1L), any(BigDecimal.class))).thenReturn(1);
        when(productMapper.updateStock(2L, 3)).thenReturn(1);
        when(orderMapper.createOrder(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(10L);
            return 1;
        });
        when(orderMapper.findById(10L)).thenReturn(savedOrder);

        Order result = orderService.createOrder(1L, 2L, "EE", 2);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(new BigDecimal("200.00"), result.getOrderPrice());
        assertEquals(2, result.getAmount());

        verify(userMapper).updateBalance(1L, new BigDecimal("800.00"));
        verify(productMapper).updateStock(2L, 3);
        verify(orderMapper).createOrder(any(Order.class));
        verify(orderMapper).findById(10L);
    }

    @Test
    void createOrder_buyerNotFound() {
        when(userMapper.getUserById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> orderService.createOrder(1L, 2L, "EE", 1)
        );

        assertEquals("Buyer not found", ex.getMessage());

        verify(productMapper, never()).findById(anyLong());
        verify(orderMapper, never()).createOrder(any());
    }

    @Test
    void createOrder_productNotFound() {
        User buyer = new User();
        buyer.setId(1L);
        buyer.setBalance(new BigDecimal("1000.00"));

        when(userMapper.getUserById(1L)).thenReturn(buyer);
        when(productMapper.findById(2L)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> orderService.createOrder(1L, 2L, "EE", 1)
        );

        assertEquals("Product not found", ex.getMessage());

        verify(orderMapper, never()).createOrder(any());
    }

    @Test
    void createOrder_invalidAmount() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> orderService.createOrder(1L, 2L, "EE", 0)
        );

        assertEquals("Amount must be greater than 0", ex.getMessage());

        verify(userMapper, never()).getUserById(anyLong());
        verify(productMapper, never()).findById(anyLong());
    }

    @Test
    void createOrder_insufficientStock() {
        User buyer = new User();
        buyer.setId(1L);
        buyer.setBalance(new BigDecimal("1000.00"));

        Product product = new Product();
        product.setId(2L);
        product.setSellerId(3L);
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(1);

        when(userMapper.getUserById(1L)).thenReturn(buyer);
        when(productMapper.findById(2L)).thenReturn(product);

        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> orderService.createOrder(1L, 2L, "EE", 2)
        );

        assertEquals("Not enough product stock", ex.getMessage());

        verify(userMapper, never()).updateBalance(anyLong(), any(BigDecimal.class));
        verify(productMapper, never()).updateStock(anyLong(), anyInt());
        verify(orderMapper, never()).createOrder(any(Order.class));
        verify(orderMapper, never()).findById(anyLong());
    }

    @Test
    void createOrder_insufficientFunds() {
        User buyer = new User();
        buyer.setId(1L);
        buyer.setBalance(new BigDecimal("0.00"));

        Product product = new Product();
        product.setId(2L);
        product.setSellerId(3L);
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(5);

        when(userMapper.getUserById(1L)).thenReturn(buyer);
        when(productMapper.findById(2L)).thenReturn(product);

        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> orderService.createOrder(1L, 2L, "EE", 2)
        );

        assertEquals("Insufficient funds", ex.getMessage());

        verify(userMapper, never()).updateBalance(anyLong(), any(BigDecimal.class));
        verify(productMapper, never()).updateStock(anyLong(), anyInt());
        verify(orderMapper, never()).createOrder(any(Order.class));
        verify(orderMapper, never()).findById(anyLong());
    }

    @Test
    void createOrder_cannotBuyYourOwnProduct() {
        User buyer = new User();
        buyer.setId(1L);
        buyer.setBalance(new BigDecimal("1000.00"));

        Product product = new Product();
        product.setId(2L);
        product.setSellerId(1L);
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(5);

        when(userMapper.getUserById(1L)).thenReturn(buyer);
        when(productMapper.findById(2L)).thenReturn(product);

        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> orderService.createOrder(1L, 2L, "EE", 2)
        );

        assertEquals("Cannot buy your own product", ex.getMessage());

        verify(userMapper, never()).updateBalance(anyLong(), any(BigDecimal.class));
        verify(productMapper, never()).updateStock(anyLong(), anyInt());
        verify(orderMapper, never()).createOrder(any(Order.class));
        verify(orderMapper, never()).findById(anyLong());
    }

    @Test
    void updateStatus_successShipped() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setSellerId(3L);
        existingOrder.setStatus("ORDERED");

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setSellerId(3L);
        updatedOrder.setStatus("SHIPPED");

        when(orderMapper.findById(1L)).thenReturn(existingOrder, updatedOrder);
        when(orderMapper.updateStatus(1L, "SHIPPED")).thenReturn(1);

        Order result = orderService.updateStatus(1L, "SHIPPED");

        assertNotNull(result);
        assertEquals("SHIPPED", result.getStatus());

        verify(orderMapper).updateStatus(1L, "SHIPPED");
        verify(userMapper, never()).updateBalance(anyLong(), any());
        verify(userMapper, never()).updateSales(anyLong(), anyInt());
    }

    @Test
    void updateStatus_invalidTransition() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setStatus("ORDERED");

        when(orderMapper.findById(1L)).thenReturn(existingOrder);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> orderService.updateStatus(1L, "COMPLETED")
        );

        assertEquals("Invalid status transition", ex.getMessage());

        verify(orderMapper, never()).updateStatus(anyLong(), anyString());
    }

    @Test
    void updateStatus_orderNotFound() {
        when(orderMapper.findById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> orderService.updateStatus(1L, "SHIPPED")
        );

        assertEquals("Order not found", ex.getMessage());

        verify(orderMapper, never()).updateStatus(anyLong(), anyString());
    }

    @Test
    void updateStatus_completed_updatesSellerBalanceAndSales() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setSellerId(3L);
        existingOrder.setOrderPrice(new BigDecimal("250.00"));
        existingOrder.setStatus("READY_TO_CLAIM");

        User seller = new User();
        seller.setId(3L);
        seller.setBalance(new BigDecimal("100.00"));
        seller.setSales(2);

        Order completedOrder = new Order();
        completedOrder.setId(1L);
        completedOrder.setSellerId(3L);
        completedOrder.setOrderPrice(new BigDecimal("250.00"));
        completedOrder.setStatus("COMPLETED");

        when(orderMapper.findById(1L)).thenReturn(existingOrder, completedOrder);
        when(orderMapper.updateStatus(1L, "COMPLETED")).thenReturn(1);
        when(userMapper.getUserById(3L)).thenReturn(seller);
        when(userMapper.updateBalance(3L, new BigDecimal("350.00"))).thenReturn(1);
        when(userMapper.updateSales(3L, 3)).thenReturn(1);

        Order result = orderService.updateStatus(1L, "COMPLETED");

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());

        verify(orderMapper).updateStatus(1L, "COMPLETED");
        verify(userMapper).updateBalance(3L, new BigDecimal("350.00"));
        verify(userMapper).updateSales(3L, 3);
    }
}