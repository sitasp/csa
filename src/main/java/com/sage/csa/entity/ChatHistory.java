package com.sage.csa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.ai.chat.messages.MessageType;

import java.time.Instant;

@Entity
@Table(name = "chat_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String chatId;
    private String userName;
    private String message;
    @Column(name = "message_type")
    private MessageType messageType;
    private Instant createdAt;
}
