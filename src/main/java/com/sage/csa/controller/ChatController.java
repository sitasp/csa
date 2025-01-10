package com.sage.csa.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        Flux<String> response =  chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .doOnNext(item -> {
                    // Replace with your logging framework (e.g., SLF4J)
                    System.out.println("Response on message: " + item); // Simple console log for demonstration
                });
        return response;
    }
}
