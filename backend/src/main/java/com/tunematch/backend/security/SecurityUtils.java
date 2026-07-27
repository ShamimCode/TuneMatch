package com.tunematch.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtils {

    public static String getCurrentUsername() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
    }
}