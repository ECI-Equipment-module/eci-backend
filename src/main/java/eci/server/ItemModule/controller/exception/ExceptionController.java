package eci.server.ItemModule.controller.exception;

import eci.server.ItemModule.exception.member.auth.*;
import eci.server.ItemModule.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 예외 사항 발생 시 "/exception/{예외}"로 리다이렉트
 */

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://6a3e-118-36-38-193.ngrok.io")
public class ExceptionController {

    private final TokenService tokenService;

    @GetMapping("/exception/entry-point")
    public void entryPoint(@RequestHeader(value = "Authorization") String accessToken) {
        /**
         * 액세스 만료
         */
        if (!tokenService.validateAccessToken(accessToken)) {
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

//    @GetMapping("/exception/refresh-expired")
//    public void refreshExpired(HttpServletRequest req) {
//        throw new RefreshExpiredException();
//        }

    }
