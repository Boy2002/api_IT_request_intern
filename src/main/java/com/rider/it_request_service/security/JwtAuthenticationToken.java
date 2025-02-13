package com.rider.it_request_service.security;

import com.rider.it_request_service.dto.UserDTO;
import java.util.List;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
