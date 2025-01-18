package com.sage.csa.service;

import com.sage.csa.dto.objects.KUser;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<KUser> getLoggedInUser();
}
