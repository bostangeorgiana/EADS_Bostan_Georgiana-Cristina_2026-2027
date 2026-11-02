package com.example.vros.service;

import com.example.vros.entity.Customer;
import com.example.vros.entity.Delivery;
import com.example.vros.repository.DeliveryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

        // un anumit client
        if (customerId != null) {
            predicates.add(cb.equal(customer.get("id"), customerId));
        }

        // o anumita zona (dreptunghi pe harta)
        if (minLat != null && maxLat != null) {
            predicates.add(cb.between(customer.<Double>get("latitude"), minLat, maxLat));
        }
        if (minLon != null && maxLon != null) {
            predicates.add(cb.between(customer.<Double>get("longitude"), minLon, maxLon));
        }

        // cantitate mai mare decat un prag
        if (minQuantity != null) {
            predicates.add(cb.greaterThan(delivery.<Integer>get("quantity"), minQuantity));
        }

        // o anumita data (toata ziua)
        if (date != null) {
            predicates.add(cb.greaterThanOrEqualTo(delivery.<LocalDateTime>get("createdAt"), date.atStartOfDay()));
            predicates.add(cb.lessThan(delivery.<LocalDateTime>get("createdAt"), date.plusDays(1).atStartOfDay()));
        }

        cq.select(delivery).where(predicates.toArray(new Predicate[0]));   // WHERE cond1 AND cond2 ...

        return entityManager.createQuery(cq).getResultList();
    }
}