package com.sage.csa.service;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.entity.UserChat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserChatService {
    Flux<UserChatDTO> getUserChatByUserId(Long userId);
    Mono<Boolean> existsByUserIdAndChatId(Long userId, String chatId);
    Mono<UserChat> save(UserChat userChat);
}
