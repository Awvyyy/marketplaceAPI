package com.example.demo.dto.response;

import java.math.BigDecimal;

public class UserResponseDto {

    private Long id;
    private String name;
    private BigDecimal balance;
    private String country;

    public UserResponseDto(Long id, String name, BigDecimal balance, String country) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
