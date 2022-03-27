package eci.server.controller.sign;

import eci.server.dto.response.Response;
import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignUpRequest;
import eci.server.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static eci.server.dto.response.Response.success;

@RestController // 1
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    Logger logger = LoggerFactory.getLogger(SignController.class);

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)

    public Response signUp(@Valid @RequestBody SignUpRequest req) { // 2
        System.out.println("11111111111111111111111111111");
        logger.info("i got it");
        logger.info("pss0");
        signService.signUp(req);
        logger.info("pss3");
        return success();
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest req) { // 3
        return success(signService.signIn(req));
    }
}