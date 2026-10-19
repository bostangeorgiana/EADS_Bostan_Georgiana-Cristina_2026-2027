package com.example.lab3.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// Prinde exceptiile aruncate la imprumut/returnare si le scrie in log.
// @Order(1) = cel mai "exterior" aspect, deci vede si exceptiile aruncate de celelalte aspecte
@Aspect
@Component
@Order(1)
public class BorrowingExceptionAspect {

    private static final Logger log = LoggerFactory.getLogger(BorrowingExceptionAspect.class);

    @AfterThrowing(
            pointcut = "execution(* com.example.lab3.library.service.BorrowingService.borrowBook(..)) || "
                     + "execution(* com.example.lab3.library.service.BorrowingService.returnBook(..))",
            throwing = "ex")
    public void logFailure(JoinPoint joinPoint, Exception ex) {
        log.error("[ERROR] operation={} args={} -> {}: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }
}
