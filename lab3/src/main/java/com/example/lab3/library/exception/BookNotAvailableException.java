package com.example.lab3.library.exception;

// Exceptie custom: Cartea nu exista sau e deja imprumutata
public class BookNotAvailableException extends BorrowingException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
