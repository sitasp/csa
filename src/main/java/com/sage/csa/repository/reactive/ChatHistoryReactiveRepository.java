package com.sage.csa.repository.reactive;

import com.sage.csa.entity.ChatHistory;
import com.sage.csa.repository.ChatHistoryRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Repository
public class ChatHistoryReactiveRepository {

    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryReactiveRepository(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    /**
     * Retrieves the last N ChatHistory entries for a given conversation ID as a reactive Flux.
     * @param conversationId the ID of the conversation
     * @param lastN the number of entries to retrieve
     * @return Flux<ChatHistory>
     */
    public Flux<ChatHistory> findByChatId(String conversationId, int lastN) {
        return Mono.fromCallable(() -> chatHistoryRepository.findByChatId(conversationId, lastN))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * Retrieves all ChatHistory entries for a given conversation ID as a reactive Flux.
     * @param conversationId the ID of the conversation
     * @return Flux<ChatHistory>
     */
    public Flux<ChatHistory> findChatHistoryByChatId(String conversationId) {
        return Mono.fromCallable(() -> chatHistoryRepository.findChatHistoryByChatId(conversationId))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }
}

