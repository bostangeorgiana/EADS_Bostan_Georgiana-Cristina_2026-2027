package com.example.lab3.library.event;

// Evenimentul (un POJO, slide 32): un membru are mai multe carti decat pragul
public record HeavyBorrowerEvent(String memberName, int borrowedBooks, int threshold) {
}
