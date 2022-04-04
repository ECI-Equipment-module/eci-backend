package eci.server.dto.sign;

public class RefreshTokenResponseFactory {
    public static RefreshTokenResponse createRefreshTokenResponse(String accessToken) {
        return new RefreshTokenResponse(accessToken);
    }
}