package com.sage.csa.service.impl;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.UserChatRepository;
import com.sage.csa.service.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChatServiceImpl implements UserChatService {

    @Autowired private UserChatRepository userChatRepository;

    @Override
    public List<UserChatDTO> getUserChatByUserName(String userName) {
        return userChatRepository.getUserChatsByUserName(userName)
                .stream().map(e -> new UserChatDTO(e.getChatId(), e.getTitle()))
                .toList();

    }

    @Override
    public boolean existsByUserNameAndChatId(String userName, String chatId) {
        return (userChatRepository.existsByUserNameAndChatId(userName, chatId) > 0);
    }

    @Override
    public UserChat save(UserChat userChat) {
        return userChatRepository.save(userChat);
    }
}
