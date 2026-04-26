package com.example.demo.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_success() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("michael");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("agatha");

        when(userMapper.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("michael", result.get(0).getName());
        assertEquals("agatha", result.get(1).getName());
    }

    @Test
    void getUserById_success() {
        User user = new User();
        user.setId(1L);
        user.setName("michael");

        when(userMapper.getUserById(1L)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("michael", result.getName());
    }

    @Test
    void getUserById_notFound() {
        when(userMapper.getUserById(1L)).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(1L)
        );

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void createUser_success() {
        when(userMapper.getUserByName("alex")).thenReturn(null);
        when(passwordEncoder.encode("alex123")).thenReturn("encoded-password");
        when(userMapper.createUser(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return 1;
        });

        User savedUser = new User();
        savedUser.setId(10L);
        savedUser.setName("alex");
        savedUser.setPasswordHash("encoded-password");
        savedUser.setBalance(BigDecimal.ZERO);
        savedUser.setCountry("EE");

        when(userMapper.getUserById(10L)).thenReturn(savedUser);

        User result = userService.createUser("alex", "alex123", "EE");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("alex", result.getName());
        assertEquals("EE", result.getCountry());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).createUser(captor.capture());

        User createdUser = captor.getValue();
        assertEquals("alex", createdUser.getName());
        assertEquals("encoded-password", createdUser.getPasswordHash());
        assertEquals("EE", createdUser.getCountry());
    }

    @Test
    void createUser_usernameAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("alex");

        when(userMapper.getUserByName("alex")).thenReturn(existingUser);

        ConflictException ex = assertThrows(
                ConflictException.class,
                () -> userService.createUser("alex", "alex123", "EE")
        );

        assertEquals("Username already exists", ex.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userMapper, never()).createUser(any(User.class));
    }

    @Test
    void createUser_blankName() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> userService.createUser("", "alex123", "EE")
        );

        assertEquals("Username cannot be null or empty", ex.getMessage());
    }

    @Test
    void loginUser_success() {
        User user = new User();
        user.setId(1L);
        user.setName("michael");
        user.setPasswordHash("encoded");

        when(userMapper.getUserByName("michael")).thenReturn(user);
        when(passwordEncoder.matches("michael123", "encoded")).thenReturn(true);

        User result = userService.loginUser("michael", "michael123");

        assertNotNull(result);
        assertEquals("michael", result.getName());

        verify(passwordEncoder).matches("michael123", "encoded");
    }

    @Test
    void loginUser_userNotFound() {
        when(userMapper.getUserByName("michael")).thenReturn(null);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> userService.loginUser("michael", "michael123")
        );

        assertEquals("Invalid username or password", ex.getMessage());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginUser_invalidPassword() {
        User user = new User();
        user.setId(1L);
        user.setName("michael");
        user.setPasswordHash("encoded");

        when(userMapper.getUserByName("michael")).thenReturn(user);
        when(passwordEncoder.matches("wrong-password", "encoded")).thenReturn(false);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> userService.loginUser("michael", "wrong-password")
        );

        assertEquals("Invalid username or password", ex.getMessage());
    }
}