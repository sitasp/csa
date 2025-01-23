package com.sage.csa.controller;

import com.sage.csa.dto.objects.KUser;
import com.sage.csa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired private UserService userService;
//    @Autowired private UserChatService userChatService;

    @GetMapping("/profile")
    public Mono<KUser> getLoggedInUser() {
        return userService.getLoggedInUser()
                .doOnNext(kUser -> System.out.println("Retrieved User: " + kUser.toString()));
    }

}
