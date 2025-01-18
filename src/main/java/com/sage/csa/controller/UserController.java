package com.sage.csa.controller;

import com.sage.csa.dto.*;
import com.sage.csa.dto.objects.KUser;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import com.sage.csa.utils.ControllerUtils;
import com.sage.csa.utils.TokenUtils;
import org.apache.tika.utils.StringUtils;
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
