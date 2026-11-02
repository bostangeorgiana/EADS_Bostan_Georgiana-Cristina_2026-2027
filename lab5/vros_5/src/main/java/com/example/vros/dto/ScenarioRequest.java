package com.example.vros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ScenarioRequest(
        @NotBlank(message = "Numele scenariului este obligatoriu")
        @Size(max = 100, message = "Numele poate avea maxim 100 de caractere")
        String name,

        Set<Long> vehicleIds,

        Set<Long> deliveryIds) {
}
