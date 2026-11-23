package com.example.vros.scenario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    // DERIVED QUERY
    Optional<Scenario> findByName(String name);
}
