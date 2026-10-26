package com.example.vros.service;

import com.example.vros.entity.Driver;
import com.example.vros.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> findAll() { return driverRepository.findAll(); }
    public Optional<Driver> findById(Long id) { return driverRepository.findById(id); }
    public Driver save(Driver driver) { return driverRepository.save(driver); }
    public void deleteById(Long id) { driverRepository.deleteById(id); }

    public Optional<Driver> findByEmail(String email) {
        return driverRepository.findByEmail(email);
    }
}