package com.rider.it_request.security;


import com.rider.it_request.dto.UserDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final UserDTO principal;

    public JwtAuthenticationToken(UserDTO userDTO, List<SimpleGrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDTO;
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
