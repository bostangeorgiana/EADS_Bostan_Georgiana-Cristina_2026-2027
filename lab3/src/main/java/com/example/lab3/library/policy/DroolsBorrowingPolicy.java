package com.example.lab3.library.policy;

import com.example.lab3.library.config.LibraryProperties;
import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.rules.BorrowRequest;
import com.example.lab3.library.rules.RuleDecision;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// Trimite detaliile imprumutului catre motorul Drools si returneaza decizia.
@Component
@ConditionalOnProperty(name = "library.policy", havingValue = "drools")
public class DroolsBorrowingPolicy implements BorrowingPolicy {

    private final KieContainer kieContainer;
    private final LibraryProperties properties;

    public DroolsBorrowingPolicy(KieContainer kieContainer, LibraryProperties properties) {
        this.kieContainer = kieContainer;
        this.properties = properties;
    }

    @Override
    public BorrowDecision evaluate(Member member, Book book, int currentlyBorrowed) {
        BorrowRequest request = new BorrowRequest(
                member.name(),
                member.role().name(),
                currentlyBorrowed,
                book.getTitle(),
                book.getType().name(),
                book.isAvailable()
        );
        RuleDecision decision = new RuleDecision();
        KieSession session = kieContainer.newKieSession();
        try {
            session.insert(request);
            session.insert(decision);
            session.fireAllRules();
            return new BorrowDecision(decision.isAllowed(), decision.getReason(), decision.getLoanDays());
        } finally {
            // Elibereaza sesiunea dupa fiecare evaluare.
            session.dispose();
        }
    }

    @Override
    public int maxBooksFor(Member member) {
        return properties.getRoleLimits().getOrDefault(member.role(), properties.getMaxBooks());
    }

    @Override
    public String description() {
        return "Drools rules from " + properties.getRulesFile();
    }
}
