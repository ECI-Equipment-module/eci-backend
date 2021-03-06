package eci.server.ItemModule.controller.exception;

import eci.server.ItemModule.exception.member.auth.AccessDeniedException;
import eci.server.ItemModule.exception.member.auth.AccessExpiredException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


/**
 * 예외 사항 발생 시 "/exception/{예외}"로 리다이렉트
 */

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class ExceptionController {

    private final TokenService tokenService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/exception/entry-point")
    public void entryPoint(@RequestHeader(value = "Authorization") String accessToken) {
        /**
         * 액세스 만료
         */
        if (!tokenService.validateAccessToken(accessToken)) {
            System.out.println("액세스가 만료");
            //throw new AccessExpiredException();
            throw new AuthenticationEntryPointException();
        }
        System.out.println("액세스 만료가 아니라 리프레시 에러야 이거는  ");
        throw new AuthenticationEntryPointException();
    }
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/exception/access-expired")
    public void accessExpired() {
        throw new AccessExpiredException();
    }


    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/exception/access-denied")
    public void accessDenied() {

        throw new AccessDeniedException();
    }

    }
