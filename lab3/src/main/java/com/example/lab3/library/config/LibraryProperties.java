package com.example.lab3.library.config;

import com.example.lab3.library.model.MemberRole;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// Proprietatile custom "library.*" din application.properties (ca la Lab 2)
@Component
@ConfigurationProperties(prefix = "library")
public class LibraryProperties {

    private String policy = "fixed";                          // library.policy: fixed | role
    private int maxBooks = 3;                                 // library.max-books
    private Map<MemberRole, Integer> roleLimits = new HashMap<>(); // library.role-limits.STUDENT ...
    private int eventThreshold = 2;                           // library.event-threshold

    public String getPolicy() { return policy; }
    public void setPolicy(String policy) { this.policy = policy; }

    public int getMaxBooks() { return maxBooks; }
    public void setMaxBooks(int maxBooks) { this.maxBooks = maxBooks; }

    public Map<MemberRole, Integer> getRoleLimits() { return roleLimits; }
    public void setRoleLimits(Map<MemberRole, Integer> roleLimits) { this.roleLimits = roleLimits; }

    public int getEventThreshold() { return eventThreshold; }
    public void setEventThreshold(int eventThreshold) { this.eventThreshold = eventThreshold; }
}
