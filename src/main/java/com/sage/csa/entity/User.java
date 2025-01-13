package com.sage.csa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@SoftDelete
public class User {
    private long userId;
    private String userName;
    private String password;
    private String phoneNumber;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "enabled", columnDefinition = "boolean default false")
    private boolean enabled;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public User(String userName, String password, String phoneNumber, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
