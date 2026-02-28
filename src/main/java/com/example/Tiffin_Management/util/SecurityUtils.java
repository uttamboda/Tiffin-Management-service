package com.example.Tiffin_Management.util;

import com.example.Tiffin_Management.security.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getTenantId();
        }
        throw new IllegalStateException("Authentication is missing or not a JwtAuthenticationToken");
    }
}
