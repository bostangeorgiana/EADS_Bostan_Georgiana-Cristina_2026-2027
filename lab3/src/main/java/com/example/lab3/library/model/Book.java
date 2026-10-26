package com.example.lab3.library.model;

import java.time.LocalDate;

// O carte. borrowedBy = id-ul membrului care o are, sau null daca e disponibila
public class Book {

    private final Long id;
    private final String title;
    private final String author;
    private final BookType type;
    private Long borrowedBy;
    private LocalDate dueDate;

    public Book(Long id, String title, String author) {
        this(id, title, author, BookType.GENERAL);
    }

    public Book(Long id, String title, String author, BookType type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public BookType getType() { return type; }
    public Long getBorrowedBy() { return borrowedBy; }
    public LocalDate getDueDate() { return dueDate; }

    public boolean isAvailable() { return borrowedBy == null; }
    public void borrowTo(Long memberId, LocalDate dueDate) {
        this.borrowedBy = memberId;
        this.dueDate = dueDate;
    }
    public void giveBack() {
        this.borrowedBy = null;
        this.dueDate = null;
    }

    @Override
    public String toString() {
        return "#" + id + " '" + title + "' [" + type + "]"
                + (isAvailable() ? " (available)" : " (borrowed by member " + borrowedBy + ", due " + dueDate + ")");
    }
}
