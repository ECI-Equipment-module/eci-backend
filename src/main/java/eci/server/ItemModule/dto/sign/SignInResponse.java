package eci.server.ItemModule.dto.sign;

import eci.server.ItemModule.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private MemberDto member;

}