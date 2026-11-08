package com.example.vros.vehicle;

import com.example.vros.vehicle.VehicleDto.VehicleRequest;
import com.example.vros.vehicle.VehicleDto.VehicleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/vehicles")
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleResponse> getAll() {
        List<VehicleResponse> result = new ArrayList<>();
        for (Vehicle vehicle : vehicleService.findAll()) {
            result.add(VehicleResponse.from(vehicle));
        }
        return result;
    }

    // CONTENT NEGOTIATION: JSON sau XML, dupa headerul Accept
    // SPRINGDOC: documentat cu @Operation si @ApiResponse
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Get a vehicle by ID", description = "Returns a single vehicle, as JSON or XML")
    @ApiResponse(responseCode = "200", description = "Vehicle found")
    @ApiResponse(responseCode = "304", description = "Not modified (same ETag as If-None-Match)")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    public ResponseEntity<VehicleResponse> getById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.findById(id);
        if (vehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(VehicleResponse.from(vehicle.get()));
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        Vehicle saved = vehicleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(VehicleResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        Optional<Vehicle> updated = vehicleService.update(id, request);
        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(VehicleResponse.from(updated.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (vehicleService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
