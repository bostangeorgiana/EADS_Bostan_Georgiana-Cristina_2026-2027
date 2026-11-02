package com.example.vros.controller;

import jakarta.validation.Valid;

import com.example.vros.dto.ScenarioRequest;
import com.example.vros.dto.ScenarioResponse;
import com.example.vros.entity.Scenario;
import com.example.vros.service.ScenarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping
    public List<ScenarioResponse> getAll() {
        List<ScenarioResponse> result = new ArrayList<>();
        for (Scenario scenario : scenarioService.findAll()) {
            result.add(ScenarioResponse.from(scenario));
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScenarioResponse> getById(@PathVariable Long id) {
        Optional<Scenario> scenario = scenarioService.findById(id);
        if (scenario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ScenarioResponse.from(scenario.get()));
    }

    @PostMapping
    public ResponseEntity<ScenarioResponse> create(@Valid @RequestBody ScenarioRequest request) {
        Scenario saved = scenarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ScenarioResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScenarioResponse> update(@PathVariable Long id, @Valid @RequestBody ScenarioRequest request) {
        Optional<Scenario> updated = scenarioService.update(id, request);
        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ScenarioResponse.from(updated.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (scenarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        scenarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
