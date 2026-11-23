package com.example.vros.security;

import com.example.vros.customer.Customer;
import com.example.vros.customer.CustomerRepository;
import com.example.vros.driver.Driver;
import com.example.vros.driver.DriverRepository;
import com.example.vros.security.AuthDto.LoginResponse;
import com.example.vros.security.AuthDto.RegisterRequest;
import com.example.vros.security.AuthDto.RegisterResponse;
import com.example.vros.user.AppUser;
import com.example.vros.user.Role;
import com.example.vros.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;
    private final long refreshExpirationMs;

    public AuthService(UserRepository userRepository, CustomerRepository customerRepository,
                       DriverRepository driverRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, RefreshTokenRepository refreshTokenRepository,
                       UserDetailsService userDetailsService,
                       @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Transactional
    public LoginResponse createTokens(UserDetails userDetails) {
        String accessToken = jwtService.generateToken(userDetails);

        AppUser user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilizator inexistent"));
        LocalDateTime expiresAt = LocalDateTime.now().plus(refreshExpirationMs, ChronoUnit.MILLIS);
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), user, expiresAt);
        refreshTokenRepository.save(refreshToken);

        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new LoginResponse(accessToken, refreshToken.getToken(), userDetails.getUsername(), role);
    }

    @Transactional
    public LoginResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalid"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expirat");
        }

        refreshTokenRepository.delete(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getUsername());
        return createTokens(userDetails);
    }

    @Transactional
    public void logout(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (request.role() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nu se pot inregistra conturi de ADMIN");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username-ul " + request.username() + " este deja folosit");
        }
        if (request.role() == Role.CUSTOMER && (request.latitude() == null || request.longitude() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un client trebuie sa aiba latitude si longitude");
        }

        AppUser user = new AppUser(request.username(), passwordEncoder.encode(request.password()), request.role());
        user = userRepository.save(user);

        Long profileId;
        if (request.role() == Role.CUSTOMER) {
            Customer customer = new Customer(request.name(), request.email(), request.phone(),
                    request.address(), request.latitude(), request.longitude());
            customer.setUser(user);
            profileId = customerRepository.save(customer).getId();
        } else {
            Driver driver = new Driver(request.name(), request.email(), request.phone());
            driver.setUser(user);
            profileId = driverRepository.save(driver).getId();
        }

        return new RegisterResponse(user.getId(), user.getUsername(), user.getRole().name(), profileId);
    }
}