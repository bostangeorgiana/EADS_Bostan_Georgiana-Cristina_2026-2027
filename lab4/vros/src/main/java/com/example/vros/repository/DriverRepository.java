package com.example.vros.repository;

import com.example.vros.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    // derived query
    Optional<Driver> findByEmail(String email);
}