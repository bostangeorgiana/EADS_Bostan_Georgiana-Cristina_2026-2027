package com.example.lab3.library;

import com.example.lab3.library.exception.BorrowingException;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.service.BorrowingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Scenariul demo, rulat la pornire (CommandLineRunner, ca la Lab 2)
@Component
public class LibraryDemoRunner implements CommandLineRunner {

    private final BorrowingService borrowingService;
    private final BookRepository bookRepository;

    public LibraryDemoRunner(BorrowingService borrowingService, BookRepository bookRepository) {
        this.borrowingService = borrowingService;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n========== HOMEWORK: LIBRARY ==========");
        System.out.println("Policy: " + borrowingService.getBorrowingPolicy().description());

        System.out.println("\n--- 1. Ana (STUDENT) tries to borrow books 1, 2, 3, 4 ---");
        for (long b = 1; b <= 4; b++) {
            borrow(1L, b);
        }

        System.out.println("\n--- 2. Elena (PREMIUM) tries to borrow books 5, 6, 7, 8 ---");
        for (long b = 5; b <= 8; b++) {
            borrow(3L, b);
        }

        System.out.println("\n--- 3. Errors: unknown member, unknown book, book already borrowed ---");
        borrow(99L, 1L);
        borrow(2L, 99L);
        borrow(2L, 1L);

        System.out.println("\n--- 4. Ana returns book 1, then Mihai borrows it ---");
        giveBack(1L, 1L);
        borrow(2L, 1L);

        System.out.println("\n--- 5. Final state ---");
        bookRepository.findAll().forEach(book -> System.out.println("   " + book));
        System.out.println("=======================================\n");
    }

    private void borrow(Long memberId, Long bookId) {
        try {
            borrowingService.borrowBook(memberId, bookId);
        } catch (BorrowingException e) {
            // exceptia e deja scrisa in log de BorrowingExceptionAspect; mergem mai departe
        }
    }

    private void giveBack(Long memberId, Long bookId) {
        try {
            borrowingService.returnBook(memberId, bookId);
        } catch (BorrowingException e) {
            // idem
        }
    }
}
