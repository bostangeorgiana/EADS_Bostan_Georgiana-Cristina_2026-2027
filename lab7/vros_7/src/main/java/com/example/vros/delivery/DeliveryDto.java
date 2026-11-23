package com.example.vros.delivery;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// DTO-urile pentru livrari: ce primim (Request) si ce trimitem (Response)
public class DeliveryDto {

    // REQUEST DTO + BEAN VALIDATION: datele trimise la POST / PUT
    public record DeliveryRequest(
            @NotNull(message = "customerId este obligatoriu")
            Long customerId,

            @NotNull(message = "Cantitatea este obligatorie")
            @Positive(message = "Cantitatea trebuie sa fie pozitiva")
            Integer quantity,

            @Size(max = 500, message = "Notele pot avea maxim 500 de caractere")
            String notes) {
    }

    // RESPONSE DTO: datele trimise inapoi clientului
    public record DeliveryResponse(Long id, Long customerId, Integer quantity, String notes, LocalDateTime createdAt) {

        public static DeliveryResponse from(Delivery delivery) {
            return new DeliveryResponse(delivery.getId(), delivery.getCustomer().getId(),
                    delivery.getQuantity(), delivery.getNotes(), delivery.getCreatedAt());
        }
    }
}
