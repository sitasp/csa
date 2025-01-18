package com.sage.csa.service.impl;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.reactive.UserChatReactiveRepository;
import com.sage.csa.service.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserChatServiceImpl implements UserChatService {

    @Autowired private UserChatReactiveRepository userChatReactiveRepository;

    @Override
    public Flux<UserChatDTO> getUserChatByUserName(String userName) {
        return userChatReactiveRepository.getUserChatsByUserName(userName)
                .map(e -> new UserChatDTO(e.getChatId(), e.getTitle()));
    }

    @Override
    public Mono<Boolean> existsByUserNameAndChatId(String userName, String chatId) {
        return userChatReactiveRepository.existsByUserNameAndChatId(userName, chatId)
                .map(count -> count > 0);
    }

    @Override
    public Mono<UserChat> save(UserChat userChat) {
        return userChatReactiveRepository.save(userChat);
    }
}
