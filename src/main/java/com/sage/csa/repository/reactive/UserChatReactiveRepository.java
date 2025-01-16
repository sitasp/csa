package com.sage.csa.repository.reactive;

import com.sage.csa.entity.UserChat;
import com.sage.csa.repository.UserChatRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
public class UserChatReactiveRepository {

    private final UserChatRepository userChatRepository;

    public UserChatReactiveRepository(UserChatRepository userChatRepository) {
        this.userChatRepository = userChatRepository;
    }

    /**
     * Retrieves UserChat by userName as a reactive Mono.
     * @param userName the Name of the user
     * @return Mono<UserChat>
     */
    public Flux<UserChat> getUserChatsByUserName(String userName) {
        return Flux.defer(() -> Flux.fromIterable(userChatRepository.getUserChatsByUserName(userName)))
                .subscribeOn(Schedulers.boundedElastic());
    }


    /**
     * Checks if a UserChat exists by userName and chatId as a reactive Mono.
     * @param userName the Name of the user
     * @param chatId the ID of the chat
     * @return Mono<Long> (count result)
     */
    public Mono<Long> existsByUserNameAndChatId(String userName, String chatId) {
        return Mono.fromCallable(() -> userChatRepository.existsByUserNameAndChatId(userName, chatId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<UserChat> save(UserChat userChat) {
        return Mono.fromCallable(() -> userChatRepository.save(userChat))
                .subscribeOn(Schedulers.boundedElastic());
    }
}

