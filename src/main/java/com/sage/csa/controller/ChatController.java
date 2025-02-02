package com.sage.csa.controller;

import com.sage.csa.dto.objects.ChatHistoryResponse;
import com.sage.csa.dto.objects.ChatMessageResponse;
import com.sage.csa.dto.request.MessageRequest;
import com.sage.csa.dto.response.ApiResponse;
import com.sage.csa.dto.response.ChatSessionResponse;
import com.sage.csa.entity.UserChat;
import com.sage.csa.service.ChatHistoryService;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import com.sage.csa.utils.ControllerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RestController
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired @Qualifier("specializedChatClient")
    private ChatClient chatClient;
    @Autowired private UserService userService;
    @Autowired private ChatHistoryService chatHistoryService;
    @Autowired private UserChatService userChatService;

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody MessageRequest messageRequest) {
        log.info("Received request: {}", messageRequest);
        String chatId = Objects.nonNull(messageRequest.getChatId()) ?
                                    messageRequest.getChatId() : UUID.randomUUID().toString();

        return chatClient.prompt()
                .user(messageRequest.getMessage())
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content()
                .map(content -> content);
    }


    @PostMapping("/chat/normal")
    public String chatNormal(@RequestBody MessageRequest messageRequest) {
        log.info("Received request: {}", messageRequest);
        String chatId = Objects.nonNull(messageRequest.getChatId()) ?
                messageRequest.getChatId() : UUID.randomUUID().toString();

        return chatClient.prompt()
                .user(messageRequest.getMessage())
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .call().content();
    }

    @GetMapping("/chat-history")
    public ApiResponse<ChatHistoryResponse> fetchChatHistory(@RequestParam String chatId){
        var loggedInUser = userService.getLoggedInUser();
        if(!userChatService.existsByUserNameAndChatId(loggedInUser.getUserName(), chatId)){
            return ControllerUtils.five00(
                    String.format("ChatId: %s is invalid for userId: %s", chatId, loggedInUser.getUserName()));
        }
        var chatHistoryDtoList = chatHistoryService.findChatHistoryByChatIdAndUsername(chatId);
        ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse();
        chatHistoryResponse.setChatMessages(chatHistoryDtoList);
        ChatSessionResponse chatSessionResponse = userChatService.getUserChatById(chatId);
        chatHistoryResponse.setChatSession(chatSessionResponse);
        return ControllerUtils.ok(chatHistoryResponse);
    }
}
