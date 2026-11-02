package com.example.vros.repository;

import com.example.vros.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // derived query
    List<Delivery> findByCustomerId(Long customerId);

    // derived query
    List<Delivery> findByQuantityGreaterThan(Integer quantity);

    // explicit jpql query
    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Delivery d WHERE d.customer.id = :customerId")
    Long getTotalQuantityForCustomer(@Param("customerId") Long customerId);

    // transactional modifying query
    @Modifying
    @Transactional
    @Query("DELETE FROM Delivery d WHERE d.createdAt < :date")
    int deleteOlderThan(@Param("date") LocalDateTime date);
}