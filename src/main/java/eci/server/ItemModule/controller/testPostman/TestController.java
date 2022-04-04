package eci.server.ItemModule.controller.testPostman;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.exception.member.auth.AccessExpiredException;
import eci.server.ItemModule.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static eci.server.ItemModule.dto.response.Response.success;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TokenService tokenService;

    @GetMapping("test")
    @ResponseStatus(HttpStatus.CREATED)
    public Response test(@RequestHeader(value = "Authorization") String accessToken) {
//            System.out.println(tokenService.validateAccessToken(accessToken));
        if(!tokenService.validateAccessToken(accessToken)){
            throw new AccessExpiredException();
        }

        String test1 = "적절한 access 일 때만 보일거야";
        Response test2 = success(test1);
        return test2;
    }

}
