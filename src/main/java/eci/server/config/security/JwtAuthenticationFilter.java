package eci.server.config.security;

import eci.server.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    /**
     * 1) Authorization 헤더에서 토큰 값을 꺼냄
     *
     * 2) 핵심 기능 : 토큰이 유효하다면 SpringSecurity가 관리해주는 컨텍스트에
     * 사용자 정보(CustomAuthenticationToken) 등록
     * == SecurityContextHolder에 있는 ContextHolder에
     * Authentication 인터페이스의 구현체 CustomAuthenticationToken 등록
     *
     * 3) 액세스 토큰과 리프레시 토큰을 구분 검증 작업
     *
     */


    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        if(validateAccessToken(token)) {
            setAccessAuthentication("access", token);
        } else if(validateRefreshToken(token)) {
            setRefreshAuthentication("refresh", token);
        }
        chain.doFilter(request, response);
    }

    private String extractToken(ServletRequest request) {
        return ((HttpServletRequest)request).getHeader("Authorization");
    }

    private boolean validateAccessToken(String token) {
        return token != null && tokenService.validateAccessToken(token);
    }

    private boolean validateRefreshToken(String token) {
        return token != null && tokenService.validateRefreshToken(token);
    }

    private void setAccessAuthentication(String type, String token) {
        String userId = tokenService.extractAccessTokenSubject(token);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(type, userDetails, userDetails.getAuthorities()));
    }

    private void setRefreshAuthentication(String type, String token) {
        String userId = tokenService.extractRefreshTokenSubject(token);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(type, userDetails, userDetails.getAuthorities()));
    }
}