package com.sage.csa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_chat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String title;
    private String chatId;
    private Instant createdAt = Instant.now();

    public UserChat(String userName, String title, String chatId) {
        this.userName = userName;
        this.title = title;
        this.chatId = chatId;
    }
}
