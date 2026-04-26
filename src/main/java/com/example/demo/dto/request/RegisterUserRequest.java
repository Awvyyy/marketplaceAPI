package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterUserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String country;

    public RegisterUserRequest() {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
