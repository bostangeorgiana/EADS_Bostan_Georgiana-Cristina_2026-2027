package com.example.vros.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// DTO-urile pentru vehicule: ce primim (Request) si ce trimitem (Response)
public class VehicleDto {

    // REQUEST DTO + BEAN VALIDATION: datele trimise la POST / PUT
    public record VehicleRequest(
            @NotBlank(message = "Numele vehiculului este obligatoriu")
            @Size(max = 100, message = "Numele poate avea maxim 100 de caractere")
            String name,

            @NotNull(message = "Capacitatea este obligatorie")
            @Positive(message = "Capacitatea trebuie sa fie pozitiva")
            Integer capacity,

            Boolean available) {
    }

    // RESPONSE DTO: datele trimise inapoi clientului
    public record VehicleResponse(Long id, String name, Integer capacity, Boolean available, Long depotId) {

        public static VehicleResponse from(Vehicle vehicle) {
            Long depotId = (vehicle.getDepot() == null) ? null : vehicle.getDepot().getId();
            return new VehicleResponse(vehicle.getId(), vehicle.getName(), vehicle.getCapacity(),
                    vehicle.getAvailable(), depotId);
        }
    }
}
