package com.example.lab3.library.aspect;

import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.repository.MemberRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// La fiecare imprumut/returnare: numele metodei, membrul, cartea si timpul de executie.
// @Around = cod inainte SI dupa metoda (slide 37)
@Aspect
@Component
@Order(2)
public class BorrowingLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(BorrowingLoggingAspect.class);

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public BorrowingLoggingAspect(MemberRepository memberRepository, BookRepository bookRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Around("execution(* com.example.lab3.library.service.BorrowingService.borrowBook(..)) || "
          + "execution(* com.example.lab3.library.service.BorrowingService.returnBook(..))")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();     // (memberId, bookId)
        Long memberId = (Long) args[0];
        Long bookId = (Long) args[1];
        String memberName = memberRepository.findById(memberId).map(Member::name).orElse("unknown #" + memberId);
        String bookTitle = bookRepository.findById(bookId).map(Book::getTitle).orElse("unknown #" + bookId);

        long start = System.nanoTime();
        String status = "OK";
        try {
            return joinPoint.proceed();          // ruleaza metoda reala
        } catch (Throwable t) {
            status = "FAILED";
            throw t;                             // nu "inghitim" exceptia
        } finally {
            double ms = (System.nanoTime() - start) / 1_000_000.0;
            log.info("[LOG] method={} member={} book='{}' time={} ms status={}",
                    joinPoint.getSignature().getName(), memberName, bookTitle,
                    String.format("%.3f", ms), status);
        }
    }
}
