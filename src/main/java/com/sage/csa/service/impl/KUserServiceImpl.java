package com.sage.csa.service.impl;

import com.sage.csa.dto.objects.KUser;
import com.sage.csa.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.sage.csa.constants.CsaConstants.K_USER;


@Service
public class KUserServiceImpl implements UserService {

    @Override
    public Mono<KUser> getLoggedInUser() {
        return Mono.deferContextual(context -> {
            if (context.hasKey(K_USER)) {
                return Mono.just(context.get(K_USER));
            }
            return Mono.empty(); // Or throw an exception if user is mandatory
        });
    }
}
