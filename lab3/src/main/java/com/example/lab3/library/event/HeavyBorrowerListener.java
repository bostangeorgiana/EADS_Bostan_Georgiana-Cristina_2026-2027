package com.example.lab3.library.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// Listener-ul: primeste evenimentul, fara sa stie cine l-a publicat (slide 31-32)
@Component
public class HeavyBorrowerListener {

    private static final Logger log = LoggerFactory.getLogger(HeavyBorrowerListener.class);

    @EventListener
    public void onHeavyBorrower(HeavyBorrowerEvent event) {
        log.warn("[EVENT] {} now has {} books borrowed (more than {})",
                event.memberName(), event.borrowedBooks(), event.threshold());
    }
}
