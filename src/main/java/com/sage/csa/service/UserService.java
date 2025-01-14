package com.sage.csa.service;

import com.sage.csa.dto.CreateUserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<String> getCurrentUserName();
    Mono<Long> getCurrentUserId();
    Mono<CreateUserResponse> createUser(String userName, String password, String mobileNumber);
    Mono<String> loginUser(String userName, String password);
}
