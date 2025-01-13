package com.sage.csa.repository.reactive;

import com.sage.csa.entity.User;
import com.sage.csa.repository.UserRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
public class UserReactiveRepository {

    private final UserRepository userRepository;

    public UserReactiveRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findByUserName(String userName) {
        return Mono.fromCallable(() -> userRepository.findByUserName(userName))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalUser -> optionalUser.map(Mono::just).orElseGet(Mono::empty));
    }

    public Mono<Long> findUserIdByUserName(String userName) {
        return Mono.fromCallable(() -> userRepository.findUserIdxByUserName(userName))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> existsByUserName(String userName) {
        return Mono.fromCallable(() -> userRepository.existsByUserName(userName))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<User> save(User user) {
        return Mono.fromCallable(() -> userRepository.save(user))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
