package com.sage.csa.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import reactor.core.publisher.Mono;

@Configuration
public class UserManagementConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveJwtDecoder jwtDecoder,
                                                                       Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter) {
        JwtReactiveAuthenticationManager jwtAuthenticationManager = new JwtReactiveAuthenticationManager(jwtDecoder);
        jwtAuthenticationManager.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        return jwtAuthenticationManager;
    }

}
