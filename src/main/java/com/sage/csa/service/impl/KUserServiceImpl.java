package com.sage.csa.service.impl;

import com.sage.csa.dto.objects.KUser;
import com.sage.csa.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.sage.csa.constants.CsaConstants.K_USER;


@Service
public class KUserServiceImpl implements UserService {

    @Override
    public KUser getLoggedInUser() {
        // Get from Security Context first
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof KUser) {
            return (KUser) authentication.getPrincipal();
        }

        // Fallback to request attributes if needed
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return (KUser) attributes.getRequest().getAttribute(K_USER);
        }

        return null;
    }
}
