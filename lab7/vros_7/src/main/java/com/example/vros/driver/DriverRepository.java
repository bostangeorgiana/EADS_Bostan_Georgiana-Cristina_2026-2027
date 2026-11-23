package com.example.vros.driver;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    // DERIVED QUERY
    Optional<Driver> findByEmail(String email);
}
