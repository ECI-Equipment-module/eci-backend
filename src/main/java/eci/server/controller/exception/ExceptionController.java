package eci.server.controller.exception;

import eci.server.exception.member.auth.AccessDeniedException;
import eci.server.exception.member.auth.AccessExpiredException;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import  eci.server.config.security.JwtAuthenticationFilter;

/**
 * 예외 사항 발생 시 "/exception/{예외}"로 리다이렉트
 */
@RestController
public class ExceptionController {


    @GetMapping("/exception/entry-point")
    public void entryPoint() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/access-expired")
    public void accessExpired() {
        throw new AccessExpiredException();
    }

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }
}