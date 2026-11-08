package com.example.vros.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// mock login
@RestController
public class LoginController {

    public record LoginRequest(String username, String password) {
    }

    // POST /login -> 200 + token fals daca datele sunt corecte, altfel 401
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.username()) && "admin123".equals(request.password())) {
            return ResponseEntity.ok(Map.of(
                    "message", "Login reusit (mock)",
                    "token", "mock-token-123"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Utilizator sau parola gresite"));
    }
}