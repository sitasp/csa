package com.sage.csa.config.security;

import com.sage.csa.config.security.filter.JwtContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig{

    private final JwtContextFilter jwtContextFilter;

    public WebSecurityConfig(JwtContextFilter jwtContextFilter) {
        this.jwtContextFilter = jwtContextFilter;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                  Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter,
                                                  CorsConfigurationSource corsConfigurationSource,
                                                  ReactiveJwtDecoder jwtDecoder) throws Exception {
        http.authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/user/create", "/api/v1/user/login").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {
                            jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                            jwt.jwtDecoder(jwtDecoder);
                        }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAfter(jwtContextFilter, SecurityWebFiltersOrder.AUTHORIZATION);;

        return http.build();
    }
}
