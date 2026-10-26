package com.example.lab3.library.policy;

// Rezultatul regulilor: daca imprumutul este permis, motivul si durata lui.
public record BorrowDecision(boolean allowed, String reason, int loanDays) {
}
