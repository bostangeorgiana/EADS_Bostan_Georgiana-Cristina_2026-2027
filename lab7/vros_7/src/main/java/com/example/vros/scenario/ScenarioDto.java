package com.example.vros.scenario;

import com.example.vros.delivery.Delivery;
import com.example.vros.vehicle.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.TreeSet;

// DTO-urile pentru scenarii: ce primim (Request) si ce trimitem (Response)
public class ScenarioDto {

    // REQUEST DTO + BEAN VALIDATION: legaturile many-to-many vin ca liste de id-uri
    public record ScenarioRequest(
            @NotBlank(message = "Numele scenariului este obligatoriu")
            @Size(max = 100, message = "Numele poate avea maxim 100 de caractere")
            String name,

            Set<Long> vehicleIds,

            Set<Long> deliveryIds) {
    }

    // RESPONSE DTO: datele trimise inapoi clientului
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
}
