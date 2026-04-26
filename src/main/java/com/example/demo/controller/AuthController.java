package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.RegisterUserRequest;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private UserResponseDto toResponse(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getBalance(), user.getCountry(), user.getSales());
    }

    @PostMapping("/registration")
    public UserResponseDto registerUser(@Valid @RequestBody RegisterUserRequest request) {
        User user = userService.createUser(request.getName(), request.getPassword(), request.getCountry());
        return toResponse(user);
        }

    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        User user = userService.loginUser(request.getName(), request.getPassword());
        return toResponse(user);
    }
}
