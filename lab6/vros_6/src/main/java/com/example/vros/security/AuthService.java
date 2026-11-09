package com.example.vros.security;

import com.example.vros.customer.Customer;
import com.example.vros.customer.CustomerRepository;
import com.example.vros.driver.Driver;
import com.example.vros.driver.DriverRepository;
import com.example.vros.security.AuthDto.RegisterRequest;
import com.example.vros.security.AuthDto.RegisterResponse;
import com.example.vros.user.AppUser;
import com.example.vros.user.Role;
import com.example.vros.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, CustomerRepository customerRepository,
                       DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
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