package com.sage.csa.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/health")
@SecurityRequirement(name = "bearerAuth")
public class HealthController {

    @GetMapping("/current")
    Mono<String> hello(@AuthenticationPrincipal Principal auth) {
        return Mono.just(String.format("Hello %s! Application is up", auth.getName()));
    }
}
