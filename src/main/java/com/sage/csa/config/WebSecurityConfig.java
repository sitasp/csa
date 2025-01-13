package com.sage.csa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
public class WebSecurityConfig {

    @Bean
    public ServerHttpSecurity serverHttpSecurity() {
        return ServerHttpSecurity.http();
    }

//    @Bean
//    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
//        return new ReactiveJwtAuthenticationConverter();
//    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                  Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter,
                                                  CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/user/create", "/api/v1/user/login").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));

        return http.build();
    }
}
