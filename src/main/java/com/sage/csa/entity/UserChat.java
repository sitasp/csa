package com.sage.csa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChat {
    @Id
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String chatId;
    private Instant createdAt = Instant.now();

    public UserChat(Long userId, String userName, String title, String chatId) {
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.chatId = chatId;
    }
}
