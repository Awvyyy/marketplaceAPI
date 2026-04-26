package com.example.demo.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_success() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("iPhone");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("AirPods");

        when(productMapper.findAll()).thenReturn(List.of(product1, product2));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("iPhone", result.get(0).getTitle());
        assertEquals("AirPods", result.get(1).getTitle());
    }

    @Test
    void getProductById_success() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("iPhone");

        when(productMapper.findById(1L)).thenReturn(product);

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getProductById_notFound() {
        when(productMapper.findById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> productService.getProductById(1L)
        );

        assertEquals("Product not found", ex.getMessage());
    }

    @Test
    void createProduct_success_withBalanceDeduction() {
        User seller = new User();
        seller.setId(1L);
        seller.setBalance(new BigDecimal("500.00"));
        seller.setSales(0);

        when(userMapper.getUserById(1L)).thenReturn(seller);
        when(userMapper.updateBalance(1L, new BigDecimal("400.00"))).thenReturn(1);
        when(productMapper.createProduct(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(10L);
            return 1;
        });

        Product savedProduct = new Product();
        savedProduct.setId(10L);
        savedProduct.setSellerId(1L);
        savedProduct.setTitle("Keyboard");
        savedProduct.setDescription("Mechanical keyboard");
        savedProduct.setPrice(new BigDecimal("100.00"));
        savedProduct.setStock(5);

        when(productMapper.findById(10L)).thenReturn(savedProduct);

        Product result = productService.createProduct(
                1L,
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("100.00"),
                5
        );

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Keyboard", result.getTitle());

        verify(userMapper).updateBalance(1L, new BigDecimal("400.00"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productMapper).createProduct(captor.capture());

        Product created = captor.getValue();
        assertEquals(1L, created.getSellerId());
        assertEquals("Keyboard", created.getTitle());
    }

    @Test
    void createProduct_success_withoutBalanceDeduction_whenSellerHasTwoOrMoreSales() {
        User seller = new User();
        seller.setId(1L);
        seller.setBalance(new BigDecimal("50.00"));
        seller.setSales(2);

        when(userMapper.getUserById(1L)).thenReturn(seller);
        when(productMapper.createProduct(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(11L);
            return 1;
        });

        Product savedProduct = new Product();
        savedProduct.setId(11L);
        savedProduct.setSellerId(1L);
        savedProduct.setTitle("Mouse");
        savedProduct.setDescription("Gaming mouse");
        savedProduct.setPrice(new BigDecimal("100.00"));
        savedProduct.setStock(3);

        when(productMapper.findById(11L)).thenReturn(savedProduct);

        Product result = productService.createProduct(
                1L,
                "Mouse",
                "Gaming mouse",
                new BigDecimal("100.00"),
                3
        );

        assertNotNull(result);
        assertEquals(11L, result.getId());

        verify(userMapper, never()).updateBalance(anyLong(), any());
    }

    @Test
    void createProduct_userNotFound() {
        when(userMapper.getUserById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> productService.createProduct(
                        1L,
                        "Mouse",
                        "Gaming mouse",
                        new BigDecimal("100.00"),
                        3
                )
        );

        assertEquals("User not found", ex.getMessage());

        verify(productMapper, never()).createProduct(any());
    }

    @Test
    void createProduct_invalidPrice() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> productService.createProduct(
                        1L,
                        "Mouse",
                        "Gaming mouse",
                        BigDecimal.ZERO,
                        3
                )
        );

        assertEquals("Price cannot be null or 0", ex.getMessage());

        verify(userMapper, never()).getUserById(anyLong());
    }

    @Test
    void createProduct_invalidStock() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> productService.createProduct(
                        1L,
                        "Mouse",
                        "Gaming mouse",
                        new BigDecimal("100.00"),
                        -1
                )
        );

        assertEquals("Stock cannot be less than 0", ex.getMessage());

        verify(userMapper, never()).getUserById(anyLong());
    }

    @Test
    void createProduct_insufficientFunds_whenSellerHasLessThanTwoSales() {
        User seller = new User();
        seller.setId(1L);
        seller.setBalance(new BigDecimal("50.00"));
        seller.setSales(0);

        when(userMapper.getUserById(1L)).thenReturn(seller);

        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> productService.createProduct(
                        1L,
                        "Mouse",
                        "Gaming mouse",
                        new BigDecimal("100.00"),
                        3
                )
        );

        assertEquals("Insufficient funds", ex.getMessage());

        verify(userMapper, never()).updateBalance(anyLong(), any());
        verify(productMapper, never()).createProduct(any());
    }
}