package com.rider.it_request_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
        int userId,
        @NotNull(message = "Username is required") @NotEmpty(message = "Username cannot be empty")
                @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
                String name,
        @NotNull(message = "Password is required") @NotEmpty(message = "Password cannot be empty")
                @Size(min = 8, message = "Password must be at least 8 characters long")
                String password,
        String role) {}
