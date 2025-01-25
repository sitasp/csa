package com.sage.csa.service;

import com.sage.csa.dto.objects.ChatHistoryResponse;
import com.sage.csa.entity.ChatHistory;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatHistoryService {

    List<ChatHistoryResponse> findChatHistoryByChatIdAndUsername(String chatId);

    List<ChatHistory> findByChatId(String conversationId, int lastN);

    List<ChatHistory> saveAll(List<ChatHistory> chatHistories);
}
