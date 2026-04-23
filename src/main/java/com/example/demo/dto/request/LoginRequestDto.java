package com.example.demo.dto.request;

public class LoginRequestDto {

    private String name;
    private String password;

    public LoginRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
