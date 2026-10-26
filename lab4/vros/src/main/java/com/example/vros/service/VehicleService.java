package com.example.vros.service;

import com.example.vros.entity.Vehicle;
import com.example.vros.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
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
}