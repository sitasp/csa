package com.sage.csa.utils;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

public class TokenUtils {
    private static final String SECRET_KEY = "your-secret-key"; // Keycloak's public key or client secret.

    public static Mono<String> getLoggedInUsername() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    var authentication = context.getAuthentication();
                    if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                        Jwt jwt = jwtAuth.getToken();
                        return jwt.getClaimAsString("preferred_username"); // Fetch the username claim
                    }
                    return null;
                });
    }
}

