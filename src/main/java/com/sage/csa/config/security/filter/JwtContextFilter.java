package com.sage.csa.config.security.filter;

import com.sage.csa.dto.objects.KUser;
import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sage.csa.constants.CsaConstants.*;

@Component
public class JwtContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String username = jwt.getClaimAsString(K_USERNAME);
            String email = jwt.getClaimAsString(K_EMAIL);
            String name = jwt.getClaimAsString(K_NAME);

            KUser kuser = KUser.builder()
                    .name(name)
                    .userName(username)
                    .emailId(email)
                    .build();

            // Store in request attributes
            request.setAttribute(K_USER, kuser);
        }

        chain.doFilter(request, response);
    }
}

