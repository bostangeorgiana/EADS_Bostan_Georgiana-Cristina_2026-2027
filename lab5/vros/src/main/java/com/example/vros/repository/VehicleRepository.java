package com.example.vros.repository;

import com.example.vros.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // derived query
    List<Vehicle> findByDepotIdAndAvailableTrue(Long depotId);

    // explicit jpql query
    @Query("SELECT v FROM Vehicle v WHERE v.available = true AND v.capacity >= :minCapacity " +
            "ORDER BY v.capacity DESC")
    List<Vehicle> findAvailableWithMinCapacity(@Param("minCapacity") Integer minCapacity);

    // transactional modifying query
    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.available = :available WHERE v.id = :id")
    int updateAvailability(@Param("id") Long id, @Param("available") Boolean available);
}