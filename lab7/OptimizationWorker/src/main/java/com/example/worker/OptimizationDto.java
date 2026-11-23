package com.example.worker;

import java.util.List;

public class OptimizationDto {

    public record VehicleData(Long id, int capacity) {
    }

    public record DeliveryData(Long id, double latitude, double longitude, int quantity) {
    }

    public record OptimizationRequest(String jobId, Long scenarioId, Double depotLatitude, Double depotLongitude,
                                      List<VehicleData> vehicles, List<DeliveryData> deliveries) {
    }

    public record OptimizationAck(String jobId, String status, String message) {
    }

    public record Route(Long vehicleId, List<Long> deliveryIds, int load, double distanceKm) {
    }

    public record OptimizationSolution(String jobId, List<Route> routes, List<Long> unassignedDeliveryIds,
                                       double totalDistanceKm) {
    }
}