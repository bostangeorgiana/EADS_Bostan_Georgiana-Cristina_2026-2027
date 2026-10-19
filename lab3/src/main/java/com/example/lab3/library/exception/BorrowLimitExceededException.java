package com.example.lab3.library.exception;

// Exceptie custom: Membrul a atins limita de carti
public class BorrowLimitExceededException extends BorrowingException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
