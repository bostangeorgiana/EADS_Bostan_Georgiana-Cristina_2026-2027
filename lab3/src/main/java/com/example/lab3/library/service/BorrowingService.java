package com.example.lab3.library.service;

import com.example.lab3.library.config.LibraryProperties;
import com.example.lab3.library.event.HeavyBorrowerEvent;
import com.example.lab3.library.exception.BookNotAvailableException;
import com.example.lab3.library.exception.BorrowLimitExceededException;
import com.example.lab3.library.exception.BorrowingException;
import com.example.lab3.library.exception.MemberNotFoundException;
import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.policy.BorrowingPolicy;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BorrowingService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;      // Spring injecteaza Fixed- sau RoleBasedPolicy
    private final ApplicationEventPublisher eventPublisher;
    private final LibraryProperties props;

    // Constructor injection pentru TOATE dependentele (slide 4, 7)
    public BorrowingService(MemberRepository memberRepository,
                            BookRepository bookRepository,
                            BorrowingPolicy borrowingPolicy,
                            ApplicationEventPublisher eventPublisher,
                            LibraryProperties props) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
        this.eventPublisher = eventPublisher;
        this.props = props;
    }

    public void borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + memberId + " does not exist"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotAvailableException("Book with id " + bookId + " does not exist"));

        // validare cu politica injectata
        int current = bookRepository.countBorrowedBy(memberId);
        if (!borrowingPolicy.canBorrow(member, current)) {
            throw new BorrowLimitExceededException(member.name() + " reached the limit of "
                    + borrowingPolicy.maxBooksFor(member) + " books");
        }

        book.borrowTo(memberId);

        // publicam evenimentul daca membrul a trecut de prag (slide 32)
        int now = current + 1;
        if (now > props.getEventThreshold()) {
            eventPublisher.publishEvent(new HeavyBorrowerEvent(member.name(), now, props.getEventThreshold()));
        }
    }

    public void returnBook(Long memberId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotAvailableException("Book with id " + bookId + " does not exist"));
        if (!memberId.equals(book.getBorrowedBy())) {
            throw new BorrowingException("Book '" + book.getTitle() + "' is not borrowed by member " + memberId);
        }
        book.giveBack();
    }

    public BorrowingPolicy getBorrowingPolicy() {
        return borrowingPolicy;
    }
}
