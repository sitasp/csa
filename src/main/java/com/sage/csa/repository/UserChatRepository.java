package com.sage.csa.repository;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    @Query("SELECT uc FROM UserChat uc WHERE uc.userId = :userId")
    List<UserChat> getUserChatsByUserId(Long userId);

    @Query("SELECT count(uc) > 0 FROM UserChat uc WHERE uc.userId = :userId AND uc.chatId = :chatId")
    Long existsByUserIdAndChatId(Long userId, String chatId);
}
