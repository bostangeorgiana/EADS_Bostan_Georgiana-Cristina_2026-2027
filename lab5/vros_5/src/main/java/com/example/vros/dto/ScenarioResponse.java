package com.example.vros.dto;

import com.example.vros.entity.Delivery;
import com.example.vros.entity.Scenario;
import com.example.vros.entity.Vehicle;

import java.util.Set;
import java.util.TreeSet;

public record ScenarioResponse(Long id, String name, Set<Long> vehicleIds, Set<Long> deliveryIds) {

    public static ScenarioResponse from(Scenario scenario) {
        Set<Long> vehicleIds = new TreeSet<>();
        for (Vehicle vehicle : scenario.getVehicles()) {
            vehicleIds.add(vehicle.getId());
        }
        Set<Long> deliveryIds = new TreeSet<>();
        for (Delivery delivery : scenario.getDeliveries()) {
            deliveryIds.add(delivery.getId());
        }
        return new ScenarioResponse(scenario.getId(), scenario.getName(), vehicleIds, deliveryIds);
    }
}
