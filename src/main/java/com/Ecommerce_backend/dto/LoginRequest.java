package com.Ecommerce_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
