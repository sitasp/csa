package com.sage.csa.controller;

import com.sage.csa.dto.*;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import com.sage.csa.utils.ControllerUtils;
import org.apache.tika.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private UserChatService userChatService;

    @PostMapping("/create")
    public Mono<ApiResponse<CreateUserResponse>> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.userName(), request.password(), request.mobileNumber())
                    .map(ControllerUtils::<CreateUserResponse>ok);
    }

    @PostMapping("/login")
    public Mono<ApiResponse<LoginUserResponse>> login(@RequestBody LoginUserRequest request){
        Mono<String> jwtToken = userService.loginUser(request.userName(), request.password());
        return jwtToken.flatMap(token -> {
            if(StringUtils.isBlank(token)){
                return Mono.just(ControllerUtils.<LoginUserResponse>five00("Invalid username or password"));
            }
            return userService.getCurrentUserId()
                    .flatMap(currentUserId ->
                            userChatService.getUserChatByUserId(currentUserId).collectList()
                    )
                    .map(e -> ControllerUtils.<LoginUserResponse>ok(new LoginUserResponse(token, e)));
        });
    }
}
