package com.sage.csa.controller;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.dto.objects.ChatHistoryResponse;
import com.sage.csa.dto.objects.KUser;
import com.sage.csa.dto.response.ApiResponse;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import com.sage.csa.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final UserChatService userChatService;


    @GetMapping("/profile")
    public KUser getLoggedInUser() {
        return userService.getLoggedInUser();
    }

    @GetMapping("/chats")
    public ApiResponse<List<UserChatDTO>> fetchUserChats(){
        var loggedInUser = userService.getLoggedInUser();
        var chatHistoryDtoList = userChatService.getUserChatByUserName(loggedInUser.getUserName());
        return ControllerUtils.ok(chatHistoryDtoList);
    }
}
