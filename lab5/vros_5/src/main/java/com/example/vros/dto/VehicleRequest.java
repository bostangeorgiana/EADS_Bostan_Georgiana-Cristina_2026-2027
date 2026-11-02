package com.example.vros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VehicleRequest(
        @NotBlank(message = "Numele vehiculului este obligatoriu")
        @Size(max = 100, message = "Numele poate avea maxim 100 de caractere")
        String name,

        @NotNull(message = "Capacitatea este obligatorie")
        @Positive(message = "Capacitatea trebuie sa fie pozitiva")
        Integer capacity,

        Boolean available) {
}
