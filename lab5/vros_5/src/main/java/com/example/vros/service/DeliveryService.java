package com.example.vros.service;

import com.example.vros.exception.ResourceNotFoundException;

import com.example.vros.dto.DeliveryRequest;
import com.example.vros.entity.Customer;
import com.example.vros.entity.Delivery;
import com.example.vros.repository.CustomerRepository;
import com.example.vros.repository.DeliveryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DeliveryService(DeliveryRepository deliveryRepository, CustomerRepository customerRepository) {
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
    }

    public List<Delivery> findAll() { return deliveryRepository.findAll(); }
    public Optional<Delivery> findById(Long id) { return deliveryRepository.findById(id); }
    public Delivery save(Delivery delivery) { return deliveryRepository.save(delivery); }
    public void deleteById(Long id) { deliveryRepository.deleteById(id); }

    public List<Delivery> findByCustomer(Long customerId) {
        return deliveryRepository.findByCustomerId(customerId);
    }

    // ACEEASI citire, dar CU CACHE
    @Cacheable("deliveriesByCustomer")
    public List<Delivery> findByCustomerCached(Long customerId) {
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

    // CREATE din DTO: livrarea apartine unui client existent
    public Delivery create(DeliveryRequest request) {
        Customer customer = findCustomer(request.customerId());
        Delivery delivery = new Delivery(customer, request.quantity(), request.notes());
        return deliveryRepository.save(delivery);
    }

    // UPDATE din DTO: Optional gol daca livrarea nu exista
    public Optional<Delivery> update(Long id, DeliveryRequest request) {
        Optional<Delivery> existing = deliveryRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Delivery delivery = existing.get();
        delivery.setCustomer(findCustomer(request.customerId()));
        delivery.setQuantity(request.quantity());
        delivery.setNotes(request.notes());
        return Optional.of(deliveryRepository.save(delivery));
    }

    private Customer findCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + customerId + " nu exista"));
    }

    // CRITERIA API: cautare dinamica, fiecare filtru e optional (null = ignorat)
    public List<Delivery> search(Long customerId,
                                 Double minLat, Double maxLat,
                                 Double minLon, Double maxLon,
                                 Integer minQuantity,
                                 LocalDate date) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Delivery> cq = cb.createQuery(Delivery.class);

        Root<Delivery> delivery = cq.from(Delivery.class);
        Join<Delivery, Customer> customer = delivery.join("customer");

        List<Predicate> predicates = new ArrayList<>();

        if (customerId != null) {
            predicates.add(cb.equal(customer.get("id"), customerId));
        }
        if (minLat != null && maxLat != null) {
            predicates.add(cb.between(customer.<Double>get("latitude"), minLat, maxLat));
        }
        if (minLon != null && maxLon != null) {
            predicates.add(cb.between(customer.<Double>get("longitude"), minLon, maxLon));
        }
        if (minQuantity != null) {
            predicates.add(cb.greaterThan(delivery.<Integer>get("quantity"), minQuantity));
        }
        if (date != null) {
            predicates.add(cb.greaterThanOrEqualTo(delivery.<LocalDateTime>get("createdAt"), date.atStartOfDay()));
            predicates.add(cb.lessThan(delivery.<LocalDateTime>get("createdAt"), date.plusDays(1).atStartOfDay()));
        }

        cq.select(delivery).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
