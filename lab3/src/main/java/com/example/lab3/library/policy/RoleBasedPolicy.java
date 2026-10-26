package com.example.lab3.library.policy;

import com.example.lab3.library.config.LibraryProperties;
import com.example.lab3.library.model.Member;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

// Limita depinde de rolul membrului (STUDENT / REGULAR / PREMIUM).
// Se creeaza doar daca library.policy=role.
@Component
@ConditionalOnProperty(name = "library.policy", havingValue = "role")
public class RoleBasedPolicy implements BorrowingPolicy {

    private final LibraryProperties props;

    public RoleBasedPolicy(LibraryProperties props) {
        this.props = props;
    }

    @Override
    public int maxBooksFor(Member member) {
        // daca rolul nu e configurat, folosim limita default
        return props.getRoleLimits().getOrDefault(member.role(), props.getMaxBooks());
    }

    @Override
    public String description() {
        return "RoleBasedPolicy - limits per role " + props.getRoleLimits();
    }
}
