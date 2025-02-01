package com.sage.csa.repository;

import com.sage.csa.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {
    @Query("SELECT uc FROM UserChat uc WHERE uc.userName = :userName order by uc.createdAt desc")
    List<UserChat> getUserChatsByUserName(String userName);

    @Query("SELECT count(uc) > 0 FROM UserChat uc WHERE uc.userName = :userName AND uc.chatId = :chatId")
    Boolean existsByUserNameAndChatId(String userName, String chatId);
}
