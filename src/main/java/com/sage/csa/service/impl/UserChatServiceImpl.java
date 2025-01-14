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
    public Flux<UserChatDTO> getUserChatByUserId(Long userId) {
        return userChatReactiveRepository.getUserChatsByUserId(userId)
                .map(e -> new UserChatDTO(e.getChatId(), e.getTitle()));
    }

    @Override
    public Mono<Boolean> existsByUserIdAndChatId(Long userId, String chatId) {
        return userChatReactiveRepository.existsByUserIdAndChatId(userId, chatId)
                .map(count -> count > 0);
    }

    @Override
    public Mono<UserChat> save(UserChat userChat) {
        return userChatReactiveRepository.save(userChat);
    }
}
