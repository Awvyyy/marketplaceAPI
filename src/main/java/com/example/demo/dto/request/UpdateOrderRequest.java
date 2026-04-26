package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateOrderRequest {

    @NotBlank
    private String status;

    public UpdateOrderRequest() {
    }

    public UpdateOrderRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}