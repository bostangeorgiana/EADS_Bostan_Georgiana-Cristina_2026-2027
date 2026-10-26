package com.example.vros.service;

import com.example.vros.entity.Delivery;
import com.example.vros.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> findAll() { return deliveryRepository.findAll(); }
    public Optional<Delivery> findById(Long id) { return deliveryRepository.findById(id); }
    public Delivery save(Delivery delivery) { return deliveryRepository.save(delivery); }
    public void deleteById(Long id) { deliveryRepository.deleteById(id); }

    public List<Delivery> findByCustomer(Long customerId) {
        return deliveryRepository.findByCustomerId(customerId);
    }

    public List<Delivery> findWithQuantityGreaterThan(Integer quantity) {
        return deliveryRepository.findByQuantityGreaterThan(quantity);
    }

    public Long getTotalQuantityForCustomer(Long customerId) {
        return deliveryRepository.getTotalQuantityForCustomer(customerId);
    }

    public int deleteOlderThan(LocalDateTime date) {
        return deliveryRepository.deleteOlderThan(date);
    }
}