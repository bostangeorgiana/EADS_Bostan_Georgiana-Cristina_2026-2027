package com.example.vros.service;

import com.example.vros.entity.Depot;
import com.example.vros.repository.DepotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepotService {

    private final DepotRepository depotRepository;

    public DepotService(DepotRepository depotRepository) {
        this.depotRepository = depotRepository;
    }

    public List<Depot> findAll() { return depotRepository.findAll(); }
    public Optional<Depot> findById(Long id) { return depotRepository.findById(id); }
    public Depot save(Depot depot) { return depotRepository.save(depot); }
    public void deleteById(Long id) { depotRepository.deleteById(id); }

    public Optional<Depot> findByName(String name) {
        return depotRepository.findByName(name);
    }
}