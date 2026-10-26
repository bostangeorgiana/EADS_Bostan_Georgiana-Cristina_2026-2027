package com.example.lab3.library.policy;

import com.example.lab3.library.model.Member;
import com.example.lab3.library.model.Book;

// Abstractia de care depinde serviciul (slide 5: depinde de abstractii, nu de implementari)
public interface BorrowingPolicy {

    // Cate carti poate avea maxim acest membru
    int maxBooksFor(Member member);

    // Poate imprumuta inca o carte?
    default boolean canBorrow(Member member, int currentlyBorrowed) {
        return currentlyBorrowed < maxBooksFor(member);
    }

    default BorrowDecision evaluate(Member member, Book book, int currentlyBorrowed) {
        if (!book.isAvailable()) {
            return new BorrowDecision(false, "Book '" + book.getTitle() + "' is not available", 0);
        }
        if (!canBorrow(member, currentlyBorrowed)) {
            return new BorrowDecision(false, member.name() + " reached the limit of "
                    + maxBooksFor(member) + " books", 0);
        }
        int loanDays = switch (member.role()) {
            case STUDENT -> 14;
            case REGULAR -> 21;
            case PREMIUM -> 30;
        };
        return new BorrowDecision(true, "", loanDays);
    }

    String description();
}
