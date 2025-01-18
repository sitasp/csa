package com.sage.csa.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/grocery")
@SecurityRequirement(name = "bearerAuth")
public class GroceryController {

    @GetMapping("/all")
    Mono<List<String>> getAllGroceries(){
        List<String> groceries = List.of("Potato", "Tomato", "Brinjal");
        return Mono.just(groceries);
    }
}
