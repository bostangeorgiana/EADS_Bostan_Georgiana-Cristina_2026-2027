package com.example.vros.security;

import com.example.vros.user.AppUser;
import com.example.vros.user.Role;
import com.example.vros.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser("admin", passwordEncoder.encode("admin123"), Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Cont ADMIN creat: admin / admin123");
        }
    }
}