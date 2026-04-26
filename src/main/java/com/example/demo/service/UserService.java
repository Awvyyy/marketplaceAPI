package com.example.demo.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userMapper.findAll();
    }

    public User getUserById(Long id){
        User user = userMapper.getUserById(id);
        if (user == null){
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public User createUser(String name, String password, String country){
        if (name == null || name.isEmpty()){
            throw new BadRequestException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()){
            throw new BadRequestException("Password cannot be null or empty");
        }
        if (country == null || country.isEmpty()){
            throw new BadRequestException("Country cannot be null or empty");
        }

        if (userMapper.getUserByName(name) != null){
            throw new ConflictException("Username already exists");
        }

        User user = new User();
        user.setName(name);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setCountry(country);

        int createdRows = userMapper.createUser(user);
        if (createdRows != 1){
            throw new ConflictException("Unable to create user");
        }
        return userMapper.getUserById(user.getId());
    }

    public User loginUser(String name, String password){
        if (name == null || name.isEmpty()){
            throw new BadRequestException("Username cannot be null or empty");
        }

        if (password == null || password.isEmpty()){
            throw new BadRequestException("Password cannot be null or empty");
        }

        User user = userMapper.getUserByName(name);
        if (user == null){
            throw new BadRequestException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())){
            throw new BadRequestException("Invalid username or password");
        }

        return user;
    }
}
