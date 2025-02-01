package com.sage.csa.service.impl;

import com.sage.csa.entity.ChatHistory;
import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.ChatHistoryRepository;
import com.sage.csa.repository.UserChatRepository;
import com.sage.csa.service.UserChatService;
import com.sage.csa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;



@Component
@RequiredArgsConstructor
@Slf4j
public class PgChatMemory implements ChatMemory {

    @Autowired private UserService userService;
    @Autowired private UserChatService userChatService;
    @Autowired private ChatHistoryRepository chatHistoryRepository;
    @Autowired private UserChatRepository userChatRepository;

    @Autowired
    @Qualifier("ordinaryClient")
    private ChatClient chatClient;


    @Override
    public void add(String conversationId, Message message) {
        add(conversationId, List.of(message));
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        var userName = userService.getLoggedInUser().getUserName();
        Boolean chatExists = userChatRepository.existsByUserNameAndChatId(userName, conversationId);
        if(!chatExists){
            String title = getTitle(messages);
            log.info("Title: {}", title);
            userChatService.save(new UserChat(userName, title, conversationId));
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
        return chatClient.prompt()
                .user( "Condense the user's input into 5-6 meaningful words that clearly convey their intent. Do not add explanations, answers, or extra context—just a direct, concise transformation of the user input."+ " <input>" + content + "</input>")
                .call()
                .content();
    }

    private ChatHistory getChatHistory(String conversationId, Message message, String username) {
        return new ChatHistory(conversationId, username,
                message.getContent(), message.getMessageType());
    }

    private Message createMessage(ChatHistory chatHistory) {
        return switch (chatHistory.getMessageType()) {
            case MessageType.USER, TOOL -> new UserMessage(chatHistory.getMessage());
            case ASSISTANT -> new AssistantMessage(chatHistory.getMessage());
            case SYSTEM -> new SystemMessage(chatHistory.getMessage());
        };
    }
}
