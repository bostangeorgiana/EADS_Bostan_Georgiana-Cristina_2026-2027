package com.example.lab3.library.service;

import com.example.lab3.library.config.LibraryProperties;
import com.example.lab3.library.event.HeavyBorrowerEvent;
import com.example.lab3.library.exception.BookNotAvailableException;
import com.example.lab3.library.exception.BorrowingException;
import com.example.lab3.library.exception.BorrowingNotAllowedException;
import com.example.lab3.library.exception.MemberNotFoundException;
import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.policy.BorrowDecision;
import com.example.lab3.library.policy.BorrowingPolicy;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowingService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;
    private final ApplicationEventPublisher eventPublisher;
    private final LibraryProperties props;

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

    public BorrowDecision borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + memberId + " does not exist"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotAvailableException("Book with id " + bookId + " does not exist"));

        // Verificam decizia politicii injectate inainte sa modificam cartea.
        int current = bookRepository.countBorrowedBy(memberId);
        BorrowDecision decision = borrowingPolicy.evaluate(member, book, current);
        if (!decision.allowed()) {
            throw new BorrowingNotAllowedException(decision.reason());
        }

        book.borrowTo(memberId, LocalDate.now().plusDays(decision.loanDays()));

        // Publicam evenimentul daca membrul a trecut de prag 
        int now = current + 1;
        if (now > props.getEventThreshold()) {
            eventPublisher.publishEvent(new HeavyBorrowerEvent(member.name(), now, props.getEventThreshold()));
        }
        return decision;
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
