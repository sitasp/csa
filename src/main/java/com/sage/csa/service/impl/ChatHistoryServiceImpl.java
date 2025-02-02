package com.sage.csa.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.csa.dto.objects.ChatMessageResponse;
import com.sage.csa.entity.ChatHistory;
import com.sage.csa.repository.ChatHistoryRepository;
import com.sage.csa.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatHistoryServiceImpl implements ChatHistoryService {
    private final ChatHistoryRepository chatHistoryRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<ChatMessageResponse> findChatHistoryByChatIdAndUsername(String chatId) {
        return chatHistoryRepository.findChatHistoryByChatId(chatId)
                .stream()
                .map(e -> objectMapper.convertValue(e, ChatMessageResponse.class))
                .toList();
    }

    @Override
    public List<ChatHistory> findByChatId(String conversationId, int lastN) {
        return chatHistoryRepository.findByChatId(conversationId, lastN);
    }

    public List<ChatHistory> saveAll(List<ChatHistory> chatHistories) {
        return chatHistoryRepository.saveAll(chatHistories);
    }
}
