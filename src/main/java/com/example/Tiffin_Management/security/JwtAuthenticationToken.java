package com.example.Tiffin_Management.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Long tenantId;

    public JwtAuthenticationToken(Object principal, Long tenantId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.tenantId = tenantId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // Not needed
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public Long getTenantId() {
        return tenantId;
    }
}
