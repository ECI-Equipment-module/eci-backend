//package eci.server.ItemModule.dto.sign;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class SignInResponseCookie {
//    public ResponseEntity<?> SignInResponseCookie(String access, String refresh, HttpServletResponse response) throws IOException {
//
//        Cookie refreshToken = new Cookie("refreshToken", refresh);
//        refreshToken.setMaxAge(7 * 24 * 60 * 60);
//        refreshToken.setSecure(true);
//        refreshToken.setHttpOnly(true);
//        refreshToken.setPath("/");
//
//        Cookie accessToken = new Cookie("accessToken", access);
//        accessToken.setMaxAge(7 * 24 * 60 * 60);
//        accessToken.setSecure(true);
//        accessToken.setHttpOnly(true);
//        accessToken.setPath("/");
//
//        response.addCookie(refreshToken);
//
//        return new ResponseEntity<>("access Token, refresh Token", HttpStatus.OK);
//    }
//}