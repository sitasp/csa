package com.sage.csa.repository;

import com.sage.csa.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    @Query("SELECT ch FROM ChatHistory ch WHERE ch.chatId = :conversationId ORDER BY ch.createdAt DESC LIMIT :lastN")
    List<ChatHistory> findByChatId(String conversationId, int lastN);

    @Query("SELECT ch FROM ChatHistory ch WHERE ch.chatId = :conversationId")
    List<ChatHistory> findChatHistoryByChatId(String conversationId);
}
