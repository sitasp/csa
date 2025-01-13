package com.sage.csa.controller;

import com.sage.csa.dto.*;
import com.sage.csa.service.UserService;
import com.sage.csa.utils.ControllerUtils;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired private UserService userService;

    @PostMapping("/create")
    public Mono<ApiResponse<CreateUserResponse>> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.userName(), request.password(), request.mobileNumber())
                    .map(ControllerUtils::ok);
    }

    @PostMapping("/login")
    public Mono<ApiResponse<LoginUserResponse>> login(@RequestBody LoginUserRequest request){

    }
}
