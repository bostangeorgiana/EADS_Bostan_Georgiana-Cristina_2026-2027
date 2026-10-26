package com.example.vros.repository;

import com.example.vros.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // derived query
    Optional<Customer> findByEmail(String email);
    List<Customer> findByNameContainingIgnoreCase(String keyword);
}