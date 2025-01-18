package com.sage.csa.config.security.filter;

import com.sage.csa.dto.objects.KUser;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.sage.csa.constants.CsaConstants.*;

@Component
public class JwtContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(context -> {
                var authentication = context.getAuthentication();
                if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                    Jwt jwt = jwtAuth.getToken();
                    String username = jwt.getClaimAsString(K_USERNAME);
                    String email = jwt.getClaimAsString(K_EMAIL);
                    String name = jwt.getClaimAsString(K_NAME);

                    // Create KUser object
                    KUser kuser = KUser.builder()
                            .name(name)
                            .userName(username)
                            .emailId(email)
                            .build();

                    // Update context with KUser
                    return chain.filter(exchange)
                            .contextWrite(ctx -> ctx.put(K_USER, kuser));
                }
                return chain.filter(exchange);
            });
    }
}

