package com.example.lab3.library.policy;

import com.example.lab3.library.model.Member;

// Abstractia de care depinde serviciul (slide 5: depinde de abstractii, nu de implementari)
public interface BorrowingPolicy {

    // Cate carti poate avea maxim acest membru
    int maxBooksFor(Member member);

    // Poate imprumuta inca o carte?
    default boolean canBorrow(Member member, int currentlyBorrowed) {
        return currentlyBorrowed < maxBooksFor(member);
    }

    String description();
}
