package eci.server.controller.exception;

import eci.server.exception.member.auth.AccessDeniedException;
import eci.server.exception.member.auth.AccessExpiredException;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import eci.server.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")

/**
 * 예외 사항 발생 시 "/exception/{예외}"로 리다이렉트
 */

@RequiredArgsConstructor
@RestController
public class ExceptionController {

    private final TokenService tokenService;

    @GetMapping("/exception/entry-point")
    public void entryPoint(@RequestHeader(value = "Authorization") String accessToken) {

        if(!tokenService.validateAccessToken(accessToken)){
            throw new AccessExpiredException();
        }

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