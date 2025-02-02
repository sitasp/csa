package com.sage.csa.service;

import com.sage.csa.dto.objects.ChatMessageResponse;
import com.sage.csa.entity.ChatHistory;

import java.util.List;

public interface ChatHistoryService {

    List<ChatMessageResponse> findChatHistoryByChatIdAndUsername(String chatId);

    List<ChatHistory> findByChatId(String conversationId, int lastN);

    List<ChatHistory> saveAll(List<ChatHistory> chatHistories);
}
