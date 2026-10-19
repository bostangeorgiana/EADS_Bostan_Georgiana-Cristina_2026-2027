package com.example.lab3.library.aspect;

import com.example.lab3.library.exception.BookNotAvailableException;
import com.example.lab3.library.exception.BorrowLimitExceededException;
import com.example.lab3.library.exception.MemberNotFoundException;
import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.policy.BorrowingPolicy;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.repository.MemberRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// INAINTE de imprumut: membrul exista? cartea e disponibila? limita nu e depasita?
// Daca nu -> exceptie custom, iar metoda borrowBook NU mai ruleaza.
@Aspect
@Component
@Order(3)
public class BorrowingValidationAspect {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;

    public BorrowingValidationAspect(MemberRepository memberRepository,
                                     BookRepository bookRepository,
                                     BorrowingPolicy borrowingPolicy) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
    }

    // args(memberId, bookId) leaga argumentele metodei de parametrii acestei metode
    @Before(value = "execution(* com.example.lab3.library.service.BorrowingService.borrowBook(..)) && args(memberId, bookId)",
            argNames = "memberId,bookId")
    public void validateBorrowing(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + memberId + " does not exist"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotAvailableException("Book with id " + bookId + " does not exist"));
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Book '" + book.getTitle() + "' is already borrowed");
        }

        int current = bookRepository.countBorrowedBy(memberId);
        if (!borrowingPolicy.canBorrow(member, current)) {
            throw new BorrowLimitExceededException(member.name() + " reached the limit of "
                    + borrowingPolicy.maxBooksFor(member) + " books");
        }
    }
}
