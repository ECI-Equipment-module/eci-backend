package eci.server.handler;


import io.jsonwebtoken.*;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
=======
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {
<<<<<<< HEAD
    /**
     * 토큰 생성, 유효성 검증,
     * 파싱, 타입 알아오기
     */
=======
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71

    private String type = "Bearer ";

    public String createToken(String encodedKey, String subject, long maxAgeSeconds) {
        Date now = new Date();
        return type + Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }

    public String extractSubject(String encodedKey, String token) {
        return parse(encodedKey, token).getBody().getSubject();
    }

    public boolean validate(String encodedKey, String token) {
        try {
            parse(encodedKey, token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String key, String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(untype(token));
    }

    private String untype(String token) {
        return token.substring(type.length());
    }

<<<<<<< HEAD
    public boolean validateTokenExceptExpiration(String token, String key) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }

=======
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
}