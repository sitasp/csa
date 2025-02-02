package com.sage.csa.service;

import com.sage.csa.dto.response.ChatSessionResponse;
import com.sage.csa.entity.UserChat;

import java.util.List;

public interface UserChatService {
    List<ChatSessionResponse> getUserChatByUserName(String userName);
    ChatSessionResponse getUserChatById(String chatId);
    boolean existsByUserNameAndChatId(String userName, String chatId);
    UserChat save(UserChat userChat);
}
