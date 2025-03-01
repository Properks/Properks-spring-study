package com.example.customformlogin.global.auth.principal;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;
    private String tokenValue;

    private JwtTokenAuthenticationToken(UserDetails principals, String tokenValue, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principals;
        this.tokenValue = tokenValue;
        super.setAuthenticated(true);
    }

    public static JwtTokenAuthenticationToken authenticate(UserDetails principals, String tokenValue, Collection<? extends GrantedAuthority> authorities) {
        return new JwtTokenAuthenticationToken(principals, tokenValue, authorities);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.tokenValue;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.tokenValue = null;
    }
}
