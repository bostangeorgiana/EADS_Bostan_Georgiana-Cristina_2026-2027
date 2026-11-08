package com.example.vros.scenario;

import com.example.vros.common.ResourceNotFoundException;
import com.example.vros.delivery.Delivery;
import com.example.vros.delivery.DeliveryRepository;
import com.example.vros.scenario.ScenarioDto.ScenarioRequest;
import com.example.vros.vehicle.Vehicle;
import com.example.vros.vehicle.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final VehicleRepository vehicleRepository;
    private final DeliveryRepository deliveryRepository;

    public ScenarioService(ScenarioRepository scenarioRepository, VehicleRepository vehicleRepository,
                           DeliveryRepository deliveryRepository) {
        this.scenarioRepository = scenarioRepository;
        this.vehicleRepository = vehicleRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public List<Scenario> findAll() { return scenarioRepository.findAll(); }
    public Optional<Scenario> findById(Long id) { return scenarioRepository.findById(id); }
    public Scenario save(Scenario scenario) { return scenarioRepository.save(scenario); }
    public void deleteById(Long id) { scenarioRepository.deleteById(id); }

    public Optional<Scenario> findByName(String name) {
        return scenarioRepository.findByName(name);
    }

    // CREATE din DTO: scenariu nou cu vehiculele si livrarile date prin id
    @Transactional
    public Scenario create(ScenarioRequest request) {
        Scenario scenario = new Scenario();
        fill(scenario, request);
        return scenarioRepository.save(scenario);
    }

    // UPDATE din DTO: Optional gol daca scenariul nu exista
    @Transactional
    public Optional<Scenario> update(Long id, ScenarioRequest request) {
        Optional<Scenario> existing = scenarioRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Scenario scenario = existing.get();
        fill(scenario, request);
        return Optional.of(scenarioRepository.save(scenario));
    }

    // copiaza numele si transforma listele de id-uri in obiecte Vehicle / Delivery
    private void fill(Scenario scenario, ScenarioRequest request) {
        scenario.setName(request.name());

        Set<Vehicle> vehicles = new HashSet<>();
        if (request.vehicleIds() != null) {
            for (Long vehicleId : request.vehicleIds()) {
                Vehicle vehicle = vehicleRepository.findById(vehicleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Vehiculul cu id " + vehicleId + " nu exista"));
                vehicles.add(vehicle);
            }
        }
        scenario.setVehicles(vehicles);

        Set<Delivery> deliveries = new HashSet<>();
        if (request.deliveryIds() != null) {
            for (Long deliveryId : request.deliveryIds()) {
                Delivery delivery = deliveryRepository.findById(deliveryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Livrarea cu id " + deliveryId + " nu exista"));
                deliveries.add(delivery);
            }
        }
        scenario.setDeliveries(deliveries);
    }
}
