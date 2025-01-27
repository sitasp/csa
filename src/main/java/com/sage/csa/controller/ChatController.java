package com.sage.csa.controller;

import com.sage.csa.dto.objects.ChatHistoryResponse;
import com.sage.csa.dto.objects.KUser;
import com.sage.csa.dto.request.MessageRequest;
import com.sage.csa.dto.response.ApiResponse;
import com.sage.csa.entity.ChatHistory;
import com.sage.csa.service.ChatHistoryService;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import com.sage.csa.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;
    private final UserService userService;
    private final UserChatService userChatService;
    private final ChatHistoryService chatHistoryService;

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody MessageRequest messageRequest) {
        log.info("Received request: {}", messageRequest);

        return chatClient.prompt()
                .user(messageRequest.getMessage())
                .stream()
                .content()
                .map(content -> content)  // No need to wrap in an object, just stream the content directly
                .doOnNext(item -> {
                    System.out.println("Response chunk: " + item);
                });
    }

    @GetMapping("/chat-history")
    public ApiResponse<List<ChatHistoryResponse>> fetchChatHistory(@RequestParam String chatId){
        var loggedInUser = userService.getLoggedInUser();
        var chatHistoryDtoList = chatHistoryService.findChatHistoryByChatIdAndUsername(chatId);
        return ControllerUtils.ok(chatHistoryDtoList);
    }
}
