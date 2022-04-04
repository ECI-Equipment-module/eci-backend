package eci.server.service.sign;


<<<<<<< HEAD
import eci.server.exception.member.auth.AccessExpiredException;
import eci.server.exception.member.sign.MemberNotFoundException;
=======
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
import eci.server.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
<<<<<<< HEAD
    /**
     * JwtHandler를 이용하여
     * 각각 토큰 종류마다,
     * 검증 & subject 추출
     */
    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}")
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}")
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("${jwt.key.refresh}")
    private String refreshKey;

    public String createAccessToken(String subject) {
        return jwtHandler.createToken(
                accessKey,
                subject,
                accessTokenMaxAgeSeconds
        );
    }

    public String createRefreshToken(String subject) {
        return jwtHandler.createToken(
                refreshKey,
                subject,
                refreshTokenMaxAgeSeconds
        );
    }

    public boolean validateAccessToken(String token) {
        return jwtHandler.validate(accessKey, token);
    }

    public boolean validateRefreshToken(String token) {
        return jwtHandler.validate(refreshKey, token);
    }

    public String extractAccessTokenSubject(String token) {
        return jwtHandler.extractSubject(accessKey, token);
    }

    public String extractRefreshTokenSubject(String token) {
        return jwtHandler.extractSubject(refreshKey, token);
    }

    public void accessTokenExpired(){
        throw new AccessExpiredException();
=======
    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}") // 1
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}") // 2
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}") // 3
    private String accessKey;

    @Value("${jwt.key.refresh}") // 4
    private String refreshKey;

    public String createAccessToken(String subject) {
        return jwtHandler.createToken(accessKey, subject, accessTokenMaxAgeSeconds);
    }

    public String createRefreshToken(String subject) {
        return jwtHandler.createToken(refreshKey, subject, refreshTokenMaxAgeSeconds);
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
    }
}