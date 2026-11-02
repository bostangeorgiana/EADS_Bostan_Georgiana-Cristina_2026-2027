package com.example.vros.repository;

import com.example.vros.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepotRepository extends JpaRepository<Depot, Long> {

    // derived query
    Optional<Depot> findByName(String name);
}