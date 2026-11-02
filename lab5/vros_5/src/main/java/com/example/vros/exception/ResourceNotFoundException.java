package com.example.vros.exception;

// EXCEPTIE CUSTOM: o resursa ceruta (client, vehicul, livrare, depou) nu exista
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
