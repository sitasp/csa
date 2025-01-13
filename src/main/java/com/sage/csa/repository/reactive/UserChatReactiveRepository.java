package com.sage.csa.repository.reactive;

import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.UserChatRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
public class UserChatReactiveRepository {

    private final UserChatRepository userChatRepository;

    public UserChatReactiveRepository(UserChatRepository userChatRepository) {
        this.userChatRepository = userChatRepository;
    }

    /**
     * Retrieves UserChat by userId as a reactive Mono.
     * @param userId the ID of the user
     * @return Mono<UserChat>
     */
    public Mono<UserChat> getUserChatsByUserId(Long userId) {
        return Mono.fromCallable(() -> userChatRepository.getUserChatsByUserId(userId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Checks if a UserChat exists by userId and chatId as a reactive Mono.
     * @param userId the ID of the user
     * @param chatId the ID of the chat
     * @return Mono<Long> (count result)
     */
    public Mono<Long> existsByUserIdAndChatId(Long userId, String chatId) {
        return Mono.fromCallable(() -> userChatRepository.existsByUserIdAndChatId(userId, chatId))
                .subscribeOn(Schedulers.boundedElastic());
    }
}

