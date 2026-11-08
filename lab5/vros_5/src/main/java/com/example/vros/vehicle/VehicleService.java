package com.example.vros.vehicle;

import com.example.vros.common.ResourceNotFoundException;
import com.example.vros.depot.Depot;
import com.example.vros.depot.DepotRepository;
import com.example.vros.vehicle.VehicleDto.VehicleRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DepotRepository depotRepository;

    public VehicleService(VehicleRepository vehicleRepository, DepotRepository depotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.depotRepository = depotRepository;
    }

    public List<Vehicle> findAll() { return vehicleRepository.findAll(); }
    public Optional<Vehicle> findById(Long id) { return vehicleRepository.findById(id); }
    public Vehicle save(Vehicle vehicle) { return vehicleRepository.save(vehicle); }
    public void deleteById(Long id) { vehicleRepository.deleteById(id); }

    public List<Vehicle> findAvailableInDepot(Long depotId) {
        return vehicleRepository.findByDepotIdAndAvailableTrue(depotId);
    }

    public List<Vehicle> findAvailableWithMinCapacity(Integer minCapacity) {
        return vehicleRepository.findAvailableWithMinCapacity(minCapacity);
    }

    public int updateAvailability(Long id, Boolean available) {
        return vehicleRepository.updateAvailability(id, available);
    }

    // CREATE din DTO: vehiculul merge in singurul depou, fara sofer
    public Vehicle create(VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setName(request.name());
        vehicle.setCapacity(request.capacity());
        if (request.available() != null) {
            vehicle.setAvailable(request.available());
        }
        vehicle.setDepot(getSingleDepot());
        return vehicleRepository.save(vehicle);
    }

    // UPDATE din DTO: Optional gol daca vehiculul nu exista
    public Optional<Vehicle> update(Long id, VehicleRequest request) {
        Optional<Vehicle> existing = vehicleRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Vehicle vehicle = existing.get();
        vehicle.setName(request.name());
        vehicle.setCapacity(request.capacity());
        if (request.available() != null) {
            vehicle.setAvailable(request.available());
        }
        return Optional.of(vehicleRepository.save(vehicle));
    }

    // presupunem un singur depou: folosim primul depou din baza de date
    private Depot getSingleDepot() {
        return depotRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Nu exista niciun depou in baza de date"));
    }
}
