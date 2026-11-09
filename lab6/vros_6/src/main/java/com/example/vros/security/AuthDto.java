package com.example.vros.security;

import com.example.vros.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthDto {

    public record LoginRequest(
            @NotBlank(message = "username este obligatoriu")
            String username,

            @NotBlank(message = "password este obligatoriu")
            String password) {
    }

    public record LoginResponse(String token, String username, String role) {
    }

    public record RegisterRequest(
            @NotBlank(message = "username este obligatoriu")
            @Size(max = 100, message = "username poate avea maxim 100 de caractere")
            String username,

            @NotBlank(message = "password este obligatoriu")
            @Size(min = 6, message = "parola trebuie sa aiba minim 6 caractere")
            String password,

            @NotNull(message = "role este obligatoriu (CUSTOMER sau DRIVER)")
            Role role,

            @NotBlank(message = "name este obligatoriu")
            String name,

            @NotBlank(message = "email este obligatoriu")
            @Email(message = "email invalid")
            String email,

            String phone,

            String address,

            Double latitude,

            Double longitude) {
    }

    public record RegisterResponse(Long userId, String username, String role, Long profileId) {
    }
}