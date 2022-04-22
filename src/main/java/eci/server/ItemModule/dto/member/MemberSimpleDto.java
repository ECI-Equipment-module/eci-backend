package eci.server.ItemModule.dto.member;

import eci.server.ItemModule.entity.member.ProfileImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSimpleDto {
    private Long id;
    private String email;
    private String username;
    private String department;
    private String contact;
    private String profileImage;
}
