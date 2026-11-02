package com.example.vros.dto;

import com.example.vros.entity.Delivery;

import java.time.LocalDateTime;

public record DeliveryResponse(Long id, Long customerId, Integer quantity, String notes, LocalDateTime createdAt) {

    public static DeliveryResponse from(Delivery delivery) {
        return new DeliveryResponse(delivery.getId(), delivery.getCustomer().getId(),
                delivery.getQuantity(), delivery.getNotes(), delivery.getCreatedAt());
    }
}
