package com.example.demo.controller;


import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.dto.request.UpdateOrderRequest;
import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private OrderResponseDto toResponse(Order order) {
        return new OrderResponseDto(order.getId(), order.getBuyerId(), order.getSellerId(), order.getProductId(),
                order.getOrderPrice(), order.getAmount(), order.getDestination(), order.getStatus(), order.getCreatedAt(), order.getUpdatedAt()
        );
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return toResponse(order);
    }

    @GetMapping()
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping()
    public OrderResponseDto createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request.getBuyerId(), request.getProductId(), request.getDestination(), request.getAmount());
        return toResponse(order);
    }

    @PatchMapping("/{id}/status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderRequest request) {
        Order order = orderService.updateStatus(id, request.getStatus());
        return toResponse(order);
    }
}
