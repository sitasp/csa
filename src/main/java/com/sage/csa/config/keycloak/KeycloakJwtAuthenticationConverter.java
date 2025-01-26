package com.sage.csa.config.keycloak;

import java.util.Collection;

import com.sage.csa.dto.objects.KUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static com.sage.csa.constants.CsaConstants.*;


public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String USERNAME_CLAIM = "preferred_username";
    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    public KeycloakJwtAuthenticationConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getClaimAsString(K_USERNAME);
        String email = jwt.getClaimAsString(K_EMAIL);
        String name = jwt.getClaimAsString(K_NAME);

        // Build KUser
        KUser kuser = KUser.builder()
                .userName(username)
                .emailId(email)
                .name(name)
                .build();

        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        return new UsernamePasswordAuthenticationToken(kuser, jwt, authorities);
    }

    private String extractUsername(Jwt jwt) {
        return jwt.hasClaim(USERNAME_CLAIM) ?
                jwt.getClaimAsString(USERNAME_CLAIM) :
                jwt.getSubject();
    }
}
