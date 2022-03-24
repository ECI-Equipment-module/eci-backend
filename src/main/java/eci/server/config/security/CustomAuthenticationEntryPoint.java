package eci.server.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;
/**
 * 리프레쉬 토큰 만료 시 핸들러
 인증되지 않은 사용자가 요청 시 작동 핸들러
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        /**
         * 토큰이 만료된 경우 예외처리
         */

        response.setStatus(SC_UNAUTHORIZED);
    }
    // 스프링 도달 전이라서 직접 상황에 맞게 응답 방식 작성 가능하나 response로 응답하도록 설정
}