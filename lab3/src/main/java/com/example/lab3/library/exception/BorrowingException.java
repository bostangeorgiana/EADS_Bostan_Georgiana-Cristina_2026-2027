package com.example.lab3.library.exception;

// Exceptie custom: Exceptia de baza pentru orice problema la imprumut/returnare
public class BorrowingException extends RuntimeException {
    public BorrowingException(String message) {
        super(message);
    }
}
