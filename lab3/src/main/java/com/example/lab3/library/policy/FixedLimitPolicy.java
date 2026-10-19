package com.example.lab3.library.policy;

import com.example.lab3.library.config.LibraryProperties;
import com.example.lab3.library.model.Member;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// Politica DEFAULT: aceeasi limita pentru toti.
// Se creeaza daca library.policy=fixed SAU daca proprietatea lipseste (matchIfMissing)
@Component
@ConditionalOnProperty(name = "library.policy", havingValue = "fixed", matchIfMissing = true)
public class FixedLimitPolicy implements BorrowingPolicy {

    private final LibraryProperties props;

    public FixedLimitPolicy(LibraryProperties props) {
        this.props = props;
    }

    @Override
    public int maxBooksFor(Member member) {
        return props.getMaxBooks();
    }

    @Override
    public String description() {
        return "FixedLimitPolicy - max " + props.getMaxBooks() + " books for every member";
    }
}
