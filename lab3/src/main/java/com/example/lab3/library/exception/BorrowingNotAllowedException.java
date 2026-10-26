package com.example.lab3.library.exception;

public class BorrowingNotAllowedException extends BorrowingException {
    public BorrowingNotAllowedException(String message) {
        super(message);
    }
}
