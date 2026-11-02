package com.example.vros.repository;

import com.example.vros.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepotRepository extends JpaRepository<Depot, Long> {

    // DERIVED QUERY
    Optional<Depot> findByName(String name);

    // DERIVED QUERY: primul depou (presupunem un singur depou)
    Optional<Depot> findFirstByOrderByIdAsc();
}
