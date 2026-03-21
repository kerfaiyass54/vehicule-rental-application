package com.projecttuto.vehicule_rental.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    public Jwt getJwt() {
        return (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public String userId() {
        return getJwt().getSubject(); // sub
    }

    public String username() {
        return getJwt().getClaim("preferred_username");
    }

    public String email() {
        return getJwt().getClaim("email");
    }

    public String sessionId() {
        return getJwt().getClaim("sid");
    }
}

