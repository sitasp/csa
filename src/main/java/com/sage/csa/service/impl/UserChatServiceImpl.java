package com.sage.csa.service.impl;

import com.sage.csa.dto.response.ChatSessionResponse;
import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.UserChatRepository;
import com.sage.csa.service.UserChatService;
import com.sage.csa.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserChatServiceImpl implements UserChatService {

    @Autowired private UserChatRepository userChatRepository;

    @Override
    public List<ChatSessionResponse> getUserChatByUserName(String userName) {

        return userChatRepository.getUserChatsByUserName(userName)
                .stream().map(e -> new ChatSessionResponse(e.getChatId(), e.getTitle(), DateUtils.getTimeFromInstant(e.getCreatedAt())))
                .toList();
    }

    @Override
    public ChatSessionResponse getUserChatById(String chatId) {
        var userChat = userChatRepository.getUserChatByChatId(chatId);
        if(Objects.isNull(userChat))
            throw new NoSuchElementException(String.format("ChatId: %s doesn't exist", chatId));
        return new ChatSessionResponse(userChat.getChatId(), userChat.getTitle(), DateUtils.getTimeFromInstant(userChat.getCreatedAt()));
    }

    @Override
    public boolean existsByUserNameAndChatId(String userName, String chatId) {
        return userChatRepository.existsByUserNameAndChatId(userName, chatId);
    }

    @Override
    public UserChat save(UserChat userChat) {
        return userChatRepository.save(userChat);
    }
}
