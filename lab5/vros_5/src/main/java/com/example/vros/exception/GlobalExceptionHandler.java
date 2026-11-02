package com.example.vros.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// TRATARE GLOBALA: se aplica tuturor controllerelor
@ControllerAdvice
public class GlobalExceptionHandler {

    // exceptia noastra custom -> 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", "404");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Bean Validation a esuat (@Valid) -> 400 Bad Request, cu mesajul pentru fiecare camp
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> body = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            body.put(error.getField(), error.getDefaultMessage());
        }
        body.put("status", "400");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
