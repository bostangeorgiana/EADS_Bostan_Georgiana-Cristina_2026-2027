package com.example.vros.common;

// EXCEPTIE CUSTOM: o resursa ceruta (client, vehicul, livrare, depou) nu exista
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
