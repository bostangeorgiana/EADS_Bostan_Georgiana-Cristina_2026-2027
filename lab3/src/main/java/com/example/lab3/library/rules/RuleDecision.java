package com.example.lab3.library.rules;

// Obiectul pe care regulile il modifica pentru a construi decizia finala.
public class RuleDecision {

    private boolean allowed = true;
    private String reason = "";
    private int loanDays;

    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getLoanDays() { return loanDays; }
    public void setLoanDays(int loanDays) { this.loanDays = loanDays; }
}
