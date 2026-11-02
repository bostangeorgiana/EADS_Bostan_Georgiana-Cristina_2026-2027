package com.example.vros.dto;

import com.example.vros.entity.Vehicle;

public record VehicleResponse(Long id, String name, Integer capacity, Boolean available, Long depotId) {

    public static VehicleResponse from(Vehicle vehicle) {
        Long depotId = (vehicle.getDepot() == null) ? null : vehicle.getDepot().getId();
        return new VehicleResponse(vehicle.getId(), vehicle.getName(), vehicle.getCapacity(),
                vehicle.getAvailable(), depotId);
    }
}
