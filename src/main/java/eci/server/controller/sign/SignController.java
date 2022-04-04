package eci.server.controller.sign;

import eci.server.dto.response.Response;
import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignUpRequest;
import eci.server.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static eci.server.dto.response.Response.success;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")

public class SignController {
    private final SignService signService;

    Logger logger = LoggerFactory.getLogger(SignController.class);

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)

    public Response signUp(@Valid SignUpRequest req) {
        signService.signUp(req);
        return success();
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid SignInRequest req) {
        return success(signService.signIn(req));
    }

    /**
     * 토큰 리프레쉬 api
     *
     * HTTP Authorization 헤더에 리프레시 토큰을 전달
     * SignService.refreshToken 수행
     *
     * @param refreshToken
     * @return success
     *
     */

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    //@RequestHeader required 옵션 기본값 true
    //헤더 값이 전달 X -> 예외
    public Response refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return success(signService.refreshToken(refreshToken));
    }


}