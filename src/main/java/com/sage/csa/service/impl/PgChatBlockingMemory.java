package com.sage.csa.service.impl;

import com.sage.csa.entity.ChatHistory;
import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.ChatHistoryRepository;
import com.sage.csa.repository.UserChatRepository;
import com.sage.csa.service.ChatHistoryService;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;



@Component
@RequiredArgsConstructor
@Slf4j
public class PgChatBlockingMemory implements ChatMemory {

    private final UserService userService;
    private final UserChatService userChatService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserChatRepository userChatRepository;

    @Override
    public void add(String conversationId, Message message) {
        ChatMemory.super.add(conversationId, message);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        var userName = userService.getLoggedInUser().getUserName();
        Long chatExists = userChatRepository.existsByUserNameAndChatId(userName, conversationId);
        if(chatExists <= 0){
            userChatService.save(new UserChat(userName, getTitle(messages), conversationId));
        }
        log.info("Adding messages to conversationId: {}", conversationId);
        var chatHistories = messages.stream()
                .map(message -> getChatHistory(conversationId, message, userName))
                .toList();
        chatHistoryRepository.saveAll(chatHistories);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        log.info("Getting chat history for conversationId: {}", conversationId);
        return chatHistoryRepository.findByChatId(conversationId, lastN).stream()
                .map(this::createMessage)
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        log.info("Clearing chat history for conversationId: {}", conversationId);
    }

    public String getTitle(List<Message> messages){
        String content = messages.getFirst().getContent();
        return content.length() <= 25 ? content : content.substring(0, 25);
    }

    private ChatHistory getChatHistory(String conversationId, Message message, String username) {
        return new ChatHistory(conversationId, username,
                message.getContent(), message.getMessageType(), Instant.now());
    }

    private Message createMessage(ChatHistory chatHistory) {
        return switch (chatHistory.getMessageType()) {
            case MessageType.USER, TOOL -> new UserMessage(chatHistory.getMessage());
            case ASSISTANT -> new AssistantMessage(chatHistory.getMessage());
            case SYSTEM -> new SystemMessage(chatHistory.getMessage());
        };
    }
}
