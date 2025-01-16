package com.sage.csa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_chat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserChat {
    @Id
    private Long id;
    private String userName;
    private String title;
    private String chatId;
    private Instant createdAt = Instant.now();
}
