package eci.server.ItemModule.controller.sign;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.dto.sign.SignInRequest;
import eci.server.ItemModule.dto.sign.SignUpRequest;
import eci.server.ItemModule.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static eci.server.ItemModule.dto.response.Response.success;

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

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestHeader(value = "cookie") String refreshToken) {

        Integer index = refreshToken.length()-1;
        String rToken = (refreshToken.toString().substring(20,index));

        Response response = success(signService.refreshToken("Bearer "+rToken));

        return response;
    }

}