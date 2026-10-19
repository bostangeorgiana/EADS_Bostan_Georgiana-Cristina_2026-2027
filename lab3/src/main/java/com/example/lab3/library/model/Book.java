package com.example.lab3.library.model;

// O carte. borrowedBy = id-ul membrului care o are, sau null daca e disponibila
public class Book {

    private final Long id;
    private final String title;
    private final String author;
    private Long borrowedBy;

    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Long getBorrowedBy() { return borrowedBy; }

    public boolean isAvailable() { return borrowedBy == null; }
    public void borrowTo(Long memberId) { this.borrowedBy = memberId; }
    public void giveBack() { this.borrowedBy = null; }

    @Override
    public String toString() {
        return "#" + id + " '" + title + "'" + (isAvailable() ? " (available)" : " (borrowed by member " + borrowedBy + ")");
    }
}
