package eci.server.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    /**
     *  CustomUserDetailsService 이용해
     *  CustomUserDetails (사용자정보) & 요청 토큰 타입 저장
     */
    private String type;
    private CustomUserDetails principal;

    public CustomAuthenticationToken(String type, CustomUserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        /**
         * custom token
         */
        super(authorities);
        this.type = type;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public CustomUserDetails getPrincipal() {
        /**
         * 인증하는 사람의 정보
         */
        return principal;
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException();
    }

    public String getType() {
        return type;
    }
}