package com.example.vros.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Profile("keycloak")
public class KeycloakController {

    @GetMapping("/profile")
    public Map<String, Object> profile(@AuthenticationPrincipal OidcUser oidcUser, Authentication authentication) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", oidcUser.getPreferredUsername());
        body.put("name", oidcUser.getFullName());
        body.put("email", oidcUser.getEmail());
        body.put("rolesInSpring", authorityNames(authentication));
        return body;
    }

    @GetMapping("/api/v1/me")
    public Map<String, Object> me(@AuthenticationPrincipal Jwt jwt, Authentication authentication) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", jwt.getClaimAsString("preferred_username"));
        body.put("rolesFromKeycloak", jwt.getClaimAsStringList("roles"));
        body.put("rolesInSpring", authorityNames(authentication));
        body.put("issuer", jwt.getClaimAsString("iss"));
        body.put("expiresAt", jwt.getExpiresAt());
        return body;
    }

    private List<String> authorityNames(Authentication authentication) {
        List<String> names = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            names.add(authority.getAuthority());
        }
        return names;
    }
}