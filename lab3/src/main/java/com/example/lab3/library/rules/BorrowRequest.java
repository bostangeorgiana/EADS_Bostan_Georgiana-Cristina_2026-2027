package com.example.lab3.library.rules;

// Datele simple pe care Drools le compara cu regulile.
public class BorrowRequest {

    private final String memberName;
    private final String memberRole;
    private final int currentlyBorrowed;
    private final String bookTitle;
    private final String bookType;
    private final boolean bookAvailable;

    public BorrowRequest(String memberName, String memberRole, int currentlyBorrowed,
                         String bookTitle, String bookType, boolean bookAvailable) {
        this.memberName = memberName;
        this.memberRole = memberRole;
        this.currentlyBorrowed = currentlyBorrowed;
        this.bookTitle = bookTitle;
        this.bookType = bookType;
        this.bookAvailable = bookAvailable;
    }

    public String getMemberName() { return memberName; }
    public String getMemberRole() { return memberRole; }
    public int getCurrentlyBorrowed() { return currentlyBorrowed; }
    public String getBookTitle() { return bookTitle; }
    public String getBookType() { return bookType; }
    public boolean isBookAvailable() { return bookAvailable; }
}
