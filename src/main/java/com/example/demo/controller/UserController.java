package com.example.demo.controller;


import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.dto.response.ProductResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    public UserController(UserService userService, OrderService orderService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
    }

    private UserResponseDto toUserResponse(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getBalance(), user.getCountry(), user.getSales());
    }

    private OrderResponseDto toOrderResponse(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getBuyerId(),
                order.getSellerId(),
                order.getProductId(),
                order.getOrderPrice(),
                order.getAmount(),
                order.getDestination(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private ProductResponseDto toProductResponse(Product product) {
        return new ProductResponseDto(product.getId(), product.getSellerId(), product.getTitle(),
                product.getDescription(), product.getPrice(), product.getStock(), product.getCreatedAt()
        );
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return toUserResponse(user);
    }

    @GetMapping("/{id}/orders")
    public List<OrderResponseDto> getOrdersByUserId(@PathVariable Long id) {
        return orderService.getOrdersByBuyerId(id).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @GetMapping("/{id}/products")
    public List<ProductResponseDto> getProductsByUserId(@PathVariable Long id) {
        return productService.getAllProductsByUserId(id).stream().map(this::toProductResponse).toList();
    }
}
