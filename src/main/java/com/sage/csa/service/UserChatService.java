package com.sage.csa.service;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.entity.UserChat;

import java.util.List;

public interface UserChatService {
    List<UserChatDTO> getUserChatByUserName(String userName);
    boolean existsByUserNameAndChatId(String userName, String chatId);
    UserChat save(UserChat userChat);
}
