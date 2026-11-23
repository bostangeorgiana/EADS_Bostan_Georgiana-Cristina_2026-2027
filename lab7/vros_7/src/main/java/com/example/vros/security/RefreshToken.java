package com.example.vros.security;

import com.example.vros.user.AppUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    private LocalDateTime expiresAt;

    public RefreshToken() {
    }

    public RefreshToken(String token, AppUser user, LocalDateTime expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public Long getId() { return id; }

    public String getToken() { return token; }

    public AppUser getUser() { return user; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
}