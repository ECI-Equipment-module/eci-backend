package eci.server.ItemModule.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 *  CustomUserDetailsService 이용해
 *  CustomUserDetails (사용자정보) & 요청 토큰 타입 저장
 */
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private String type;
    private CustomUserDetails principal;

    /**
     * custom token
     */
    public CustomAuthenticationToken(CustomUserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    /**
     * 인증하는 사람의 정보
     */
    public CustomUserDetails getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException();
    }
}
