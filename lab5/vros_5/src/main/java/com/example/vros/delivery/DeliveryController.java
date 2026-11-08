package com.example.vros.delivery;

import com.example.vros.delivery.DeliveryDto.DeliveryRequest;
import com.example.vros.delivery.DeliveryDto.DeliveryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/deliveries")
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public List<DeliveryResponse> getAll() {
        List<DeliveryResponse> result = new ArrayList<>();
        for (Delivery delivery : deliveryService.findAll()) {
            result.add(DeliveryResponse.from(delivery));
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getById(@PathVariable Long id) {
        Optional<Delivery> delivery = deliveryService.findById(id);
        if (delivery.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DeliveryResponse.from(delivery.get()));
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody DeliveryRequest request) {
        Delivery saved = deliveryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeliveryResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponse> update(@PathVariable Long id, @Valid @RequestBody DeliveryRequest request) {
        Optional<Delivery> updated = deliveryService.update(id, request);
        if (updated.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DeliveryResponse.from(updated.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (deliveryService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        deliveryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
