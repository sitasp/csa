package com.sage.csa.service;

import com.sage.csa.dto.UserChatDTO;
import com.sage.csa.entity.UserChat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserChatService {
    Flux<UserChatDTO> getUserChatByUserName(String userName);
    Mono<Boolean> existsByUserNameAndChatId(String userName, String chatId);
    Mono<UserChat> save(UserChat userChat);
}
