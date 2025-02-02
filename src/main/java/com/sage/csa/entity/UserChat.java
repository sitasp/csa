package com.sage.csa.entity;

import com.sage.csa.dto.enums.ETitleType;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "title_type")
    private ETitleType titleType;

    private Instant createdAt = Instant.now();

    public UserChat(String userName, String title, String chatId) {
        this.userName = userName;
        this.title = title;
        this.chatId = chatId;
    }

    public UserChat(String userName, String title, String chatId, ETitleType titleType) {
        this.userName = userName;
        this.title = title;
        this.chatId = chatId;
        this.titleType = titleType;
    }
}
