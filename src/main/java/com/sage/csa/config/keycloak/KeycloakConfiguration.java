package com.sage.csa.config.keycloak;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class KeycloakConfiguration {

    @Bean
    Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter(
            @Value("${app.security.clientId}") String clientId
    ) {
        return new KeycloakGrantedAuthoritiesConverter(clientId);
    }

    @Bean
    Converter<Jwt, AbstractAuthenticationToken> keycloakJwtAuthenticationConverter(
            Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter
    ) {
        return new KeycloakJwtAuthenticationConverter(authoritiesConverter);
    }

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri
    ) {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
