package com.sage.csa.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.csa.dto.objects.ChatHistoryResponse;
import com.sage.csa.entity.ChatHistory;
import com.sage.csa.repository.reactive.ChatHistoryReactiveRepository;
import com.sage.csa.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatHistoryServiceImpl implements ChatHistoryService {
    private final ChatHistoryReactiveRepository chatHistoryReactiveRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<ChatHistoryResponse> findChatHistoryByChatIdAndUsername(String chatId) {
        return chatHistoryReactiveRepository.findChatHistoryByChatId(chatId)
                .map(e -> objectMapper.convertValue(e, ChatHistoryResponse.class));
    }

    @Override
    public Flux<ChatHistory> findByChatId(String conversationId, int lastN) {
        return chatHistoryReactiveRepository.findByChatId(conversationId, lastN);
    }

    public Flux<ChatHistory> saveAll(Flux<ChatHistory> chatHistories) {
        return chatHistories
                .buffer(100)
                .flatMap(batch ->
                                chatHistoryReactiveRepository.saveAll(batch)
                                        .onErrorResume(e -> {
                                            log.error("Failed to save batch", e);
                                            return Flux.empty(); // Or retry/fallback logic
                                        }),
                        4 // Concurrency
                );
    }
}
