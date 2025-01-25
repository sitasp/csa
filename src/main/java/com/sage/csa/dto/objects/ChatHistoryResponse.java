package com.sage.csa.dto.objects;

import lombok.Data;
import org.springframework.ai.chat.messages.MessageType;

import java.time.Instant;


@Data
public class ChatHistoryResponse {
    private String message;
    private MessageType messageType;
    private Instant createdAt;
}
