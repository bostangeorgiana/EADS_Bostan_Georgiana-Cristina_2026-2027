package com.example.lab3.library.exception;

// Exceptie custom: Membrul nu exista
public class MemberNotFoundException extends BorrowingException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
