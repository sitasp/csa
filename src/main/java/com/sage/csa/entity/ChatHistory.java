package com.sage.csa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.chat.messages.MessageType;

import java.time.Instant;

@Table(name = "chat_history")
@Getter
@Setter
public class ChatHistory {
    @Id
    private String chatId;              // TODO: use it as a primary key
    private long userId;
    private String userName;
    private String message;
    @Column(name = "message_type")
    private MessageType messageType;
    private Instant createdAt;
}
