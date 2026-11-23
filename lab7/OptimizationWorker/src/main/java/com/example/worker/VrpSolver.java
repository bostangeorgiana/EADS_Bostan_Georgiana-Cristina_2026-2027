package com.example.worker;

import com.example.worker.OptimizationDto.DeliveryData;
import com.example.worker.OptimizationDto.OptimizationRequest;
import com.example.worker.OptimizationDto.OptimizationSolution;
import com.example.worker.OptimizationDto.Route;
import com.example.worker.OptimizationDto.VehicleData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VrpSolver {

    public OptimizationSolution solve(OptimizationRequest request) {
        List<DeliveryData> remaining = new ArrayList<>(request.deliveries());
        List<Route> routes = new ArrayList<>();
        double totalDistance = 0;

        for (VehicleData vehicle : request.vehicles()) {
            double currentLat = request.depotLatitude();
            double currentLon = request.depotLongitude();
            int load = 0;
            double distance = 0;
            List<Long> deliveryIds = new ArrayList<>();

            while (true) {
                DeliveryData nearest = null;
                double nearestDistance = Double.MAX_VALUE;
                for (DeliveryData delivery : remaining) {
                    if (load + delivery.quantity() > vehicle.capacity()) {
                        continue;
                    }
                    double d = distanceKm(currentLat, currentLon, delivery.latitude(), delivery.longitude());
                    if (d < nearestDistance) {
                        nearestDistance = d;
                        nearest = delivery;
                    }
                }
                if (nearest == null) {
                    break;
                }
                deliveryIds.add(nearest.id());
                load += nearest.quantity();
                distance += nearestDistance;
                currentLat = nearest.latitude();
                currentLon = nearest.longitude();
                remaining.remove(nearest);
            }

            if (!deliveryIds.isEmpty()) {
                distance += distanceKm(currentLat, currentLon, request.depotLatitude(), request.depotLongitude());
                distance = Math.round(distance * 100) / 100.0;
                routes.add(new Route(vehicle.id(), deliveryIds, load, distance));
                totalDistance += distance;
            }
        }

        List<Long> unassigned = new ArrayList<>();
        for (DeliveryData delivery : remaining) {
            unassigned.add(delivery.id());
        }
        totalDistance = Math.round(totalDistance * 100) / 100.0;
        return new OptimizationSolution(request.jobId(), routes, unassigned, totalDistance);
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}