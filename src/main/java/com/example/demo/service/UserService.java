package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers(){
        return userMapper.findAll();
    }

    public User getUserById(Long id){
        User user = userMapper.getUserById(id);
        if (user == null){
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public User createUser(String name, String passwordHash, String country){
        if (name == null || name.isEmpty()){
            throw new RuntimeException("Username cannot be null or empty");
        }
        if (passwordHash == null || passwordHash.isEmpty()){
            throw new RuntimeException("Password cannot be null or empty");
        }
        if (country == null || country.isEmpty()){
            throw new RuntimeException("Country cannot be null or empty");
        }

        User user = new User();
        user.setName(name);
        user.setPasswordHash(passwordHash);
        user.setBalance(BigDecimal.ZERO);
        user.setCountry(country);

        int createdRows = userMapper.createUser(user);
        if (createdRows != 1){
            throw new RuntimeException("Unable to create user");
        }
        return userMapper.getUserById(user.getId());
    }

    public User loginUser(String name, String password){
        if (name == null || name.isEmpty()){
            throw new RuntimeException("Username cannot be null or empty");
        }

        if (password == null || password.isEmpty()){
            throw new RuntimeException("Password cannot be null or empty");
        }

        User user = userMapper.findUserByName(name);
        if (user == null){
            throw new RuntimeException("User not found");
        }

        if (!user.getPasswordHash().equals(password)){
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }
}
