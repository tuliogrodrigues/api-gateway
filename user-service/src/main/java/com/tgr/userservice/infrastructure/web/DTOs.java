package com.tgr.userservice.infrastructure.web;

import com.tgr.userservice.domain.user.Models;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public interface DTOs {

    record UserRequest(
            @NotBlank(message = "Name is required")
            String name,
            @NotBlank(message = "Last name is required")
            String lastName,
            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format")
            String email,
            @NotBlank(message = "Tax ID is required")
            String taxId,
            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
            Models.Role role) {}

    record UserResponse(
            Long id,
            String name,
            String lastName,
            String email,
            String taxId,
            String status,
            String role,
            LocalDateTime createdAt,
            LocalDateTime lastLogin) {}

    record LoginRequest(
            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format")
            String email,
            @NotBlank(message = "Password is required")
            String password) {}

    record LoginResponse(
            String token,
            String type,
            Long expiresIn,
            DTOs.UserResponse user) {}
}