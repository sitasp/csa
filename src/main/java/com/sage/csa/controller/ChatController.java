package com.sage.csa.controller;

import com.sage.csa.dto.MessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody MessageRequest messageRequest) {
        log.info("Received request: {}", messageRequest);

        return chatClient.prompt()
                .user(messageRequest.getMessage())
                .stream()
                .content()
                .map(content -> content)  // No need to wrap in an object, just stream the content directly
                .doOnNext(item -> {
                    // Log or process the string chunk
                    System.out.println("Response chunk: " + item);
                });
    }
}
