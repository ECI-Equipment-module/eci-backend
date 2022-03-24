package eci.server.config.security;

import eci.server.exception.member.auth.AccessExpiredException;
import eci.server.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * 2) 핵심 기능 : 액세스 토큰이 유효할 때만, SpringSecurity 관리해주는 컨텍스트에 사용자 정보 저장
     *
     * 사용자 정보(CustomAuthenticationToken) 등록
     * == SecurityContextHolder에 있는 ContextHolder에
     * Authentication 인터페이스의 구현체 CustomAuthenticationToken 등록
     *
     */

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    public static boolean accessTF;

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        accessTF = false;
        String token = extractToken(request);
        if(validateToken(token)) {
            // SecurityContext에 Authentication 객체 저장
            setAuthentication(token);
        }
        chain.doFilter(request, response);
    }

    private String extractToken(ServletRequest request) {
        return ((HttpServletRequest)request).getHeader("Authorization");
    }

    private boolean validateToken(String token) {
        return(token != null && tokenService.validateAccessToken(token));

    }

    private void setAuthentication(String token) {
        String userId = tokenService.extractAccessTokenSubject(token);
        if(userId == null){
            throw new AccessExpiredException();
        }

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(
                new CustomAuthenticationToken(
                        userDetails, userDetails.getAuthorities()
                )
        );
    }

}