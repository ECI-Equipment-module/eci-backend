package eci.server.controller.exception;

import eci.server.exception.member.auth.AccessDeniedException;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {
    /**
     * 예외 사항 발생 시 "/exception/{예외}"로 리다이렉트
     */

    @GetMapping("/exception/entry-point")
    public void entryPoint() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }
}