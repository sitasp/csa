package com.sage.csa.service.impl;

import com.sage.csa.dto.CreateUserResponse;
import com.sage.csa.entity.User;
import com.sage.csa.entity.UserRole;
import com.sage.csa.repository.UserRepository;
import com.sage.csa.repository.reactive.UserReactiveRepository;
import com.sage.csa.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired private UserReactiveRepository userReactiveRepository;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtEncoder jwtEncoder;

    @Override
    public Mono<String> getCurrentUserName() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName);
    }

    @Override
    public Mono<Long> getCurrentUserId() {
        return getCurrentUserName()
                .filter(StringUtils::isNotBlank)
                .flatMap(userReactiveRepository::findUserIdByUserName)
                .doOnNext(userId -> log.info("Fetched user ID: {}", userId))
                .doOnError(error -> log.error("Error occurred while fetching user ID", error))
                .switchIfEmpty(Mono.empty());
    }


    @Override
    public Mono<CreateUserResponse> createUser(String userName, String password, String mobileNumber) {
        if(Boolean.TRUE.equals(userReactiveRepository.existsByUserName(userName).block())){
            throw new IllegalArgumentException("User already exists");
        }
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            throw new IllegalArgumentException("Username and password are required");
        }
        Mono<User> createdUserMono = userReactiveRepository.save(new User(userName, password, mobileNumber, UserRole.USER));
        return createdUserMono.map(user -> new CreateUserResponse(user.getUserId(), user.getUserName()));
    }

    @Override
    public Mono<String> loginUser(String userName, String password) {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            throw new IllegalArgumentException("Username and password are required");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password);;
        var authUser = authenticationManager.authenticate(authentication);
        ReactiveSecurityContextHolder.withAuthentication(authentication);
        return getJwtToken(userName, authUser);
    }

    private Mono<String> getJwtToken(String userName, Authentication authUser) {
        return Mono.just(
                jwtEncoder.encode(
                        JwtEncoderParameters.from(JwtClaimsSet.builder()
                                .subject(userName)
                                .issuer(Project)
                ))
        )
    }
}
