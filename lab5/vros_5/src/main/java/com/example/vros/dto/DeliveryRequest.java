package com.example.vros.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DeliveryRequest(
        @NotNull(message = "customerId este obligatoriu")
        Long customerId,

        @NotNull(message = "Cantitatea este obligatorie")
        @Positive(message = "Cantitatea trebuie sa fie pozitiva")
        Integer quantity,

        @Size(max = 500, message = "Notele pot avea maxim 500 de caractere")
        String notes) {
}
