package com.sage.csa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.chat.messages.MessageType;

import java.time.Instant;

@Entity
@Table(name = "chat_history")
@Getter
@Setter
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String chatId;
    private long userId;
    private String userName;
    private String message;
    @Column(name = "message_type")
    private MessageType messageType;
    private Instant createdAt;
}
