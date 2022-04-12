package eci.server.ItemModule.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponse {
    /**
     * 액세스 토큰 가져옴
     */
    private String accessToken;
}