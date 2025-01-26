package com.sage.csa.config.security.filter;

import com.sage.csa.dto.objects.KUser;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sage.csa.constants.CsaConstants.*;

@Component
public class JwtContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
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

            request.setAttribute(K_USER, kuser);
        }

        chain.doFilter(request, response);
    }
}

