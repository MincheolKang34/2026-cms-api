package com.malgn.configure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다.");
        }

        return userDetails;
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    public static String getCurrentUserRole() {
        return getCurrentUser().getRole();
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(getCurrentUserRole());
    }
}