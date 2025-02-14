package com.rider.it_request_service.security;

import java.util.List;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final UserDetails principal;

    public JwtAuthenticationToken(
            UserDetails userDetails, List<SimpleGrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDetails;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return null; // หรือสิ่งที่เหมาะสม
    }
}
