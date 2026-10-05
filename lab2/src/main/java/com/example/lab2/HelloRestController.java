package com.example.lab2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // <- This class handles web requests
public class HelloRestController {

    // Reading configuration in code
    // "Hello" is the default message, if not set externally
    @Value("${greeting-message:Hello}")
    private String greetingMessage;

    @GetMapping("/hello")
    public String sayHello() {
        return greetingMessage;
    }
}