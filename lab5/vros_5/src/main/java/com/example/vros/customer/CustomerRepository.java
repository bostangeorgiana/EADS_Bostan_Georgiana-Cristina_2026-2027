package com.example.vros.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // DERIVED QUERY
    Optional<Customer> findByEmail(String email);

    // DERIVED QUERY
    List<Customer> findByNameContainingIgnoreCase(String keyword);
}
