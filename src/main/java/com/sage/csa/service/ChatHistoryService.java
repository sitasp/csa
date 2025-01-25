package com.sage.csa.service;

import com.sage.csa.dto.objects.ChatHistoryResponse;
import com.sage.csa.entity.ChatHistory;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatHistoryService {

    Flux<ChatHistoryResponse> findChatHistoryByChatIdAndUsername(String chatId);

    Flux<ChatHistory> findByChatId(String conversationId, int lastN);

    Flux<ChatHistory> saveAll(Flux<ChatHistory> chatHistories);
}
