package eci.server.ItemModule.controller.sign;

import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.dto.sign.SignInRequest;

import eci.server.ItemModule.dto.sign.SignInResponse;
import eci.server.ItemModule.dto.sign.SignUpRequest;
import eci.server.ItemModule.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URLEncoder;

import static eci.server.ItemModule.dto.response.Response.success;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")

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
    @ResponseBody
    public Response signIn(@Valid SignInRequest req, HttpServletResponse response) throws IOException {

        SignInResponse signInResponse = signService.signIn(req);
        String refreshToken = signInResponse.getRefreshToken();
        String accessToken = signInResponse.getAccessToken();
        MemberDto memberDto = signInResponse.getMember();
        refreshToken = URLEncoder.encode(refreshToken, "utf-8");

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);
        response.setHeader("Set-Cookieeee", "Test1="+refreshCookie+"; Secure; SameSite=None");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                        .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return success(new SignInResponse(accessToken, "httponly", memberDto));
    }


    /**
     * 토큰 리프레쉬 api
     * <p>
     * HTTP Authorization 헤더에 리프레시 토큰을 전달
     * SignService.refreshToken 수행
     * refreshToken
     *
     * @param
     * @return success
     */

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    /**
     *@RequestHeader required 옵션 기본값 true
     *헤더 값이 전달 X -> 예외
     * 쿠키값에 있는 토큰을 갖고와야 함
     *
     * 리프레시 만료 아니라면 : 리턴 값 액세스토큰
     * 리프레시 만료라면 : 다시 로그인 요청
     */
    /**
     *@RequestHeader required 옵션 기본값 true
     *헤더 값이 전달 X -> 예외
     * 쿠키값에 있는 토큰을 갖고와야 함
     *
     * 리프레시 만료 아니라면 : 리턴 값 액세스토큰
     * 리프레시 만료라면 : 다시 로그인 요청
     */
    public Response refreshToken(@RequestHeader(value = "cookie") String refreshToken) {

        Integer index = refreshToken.length()-1;
        String rToken = (refreshToken.toString().substring(20,index));
        return success(signService.refreshToken("Bearer "+rToken));
    }

}