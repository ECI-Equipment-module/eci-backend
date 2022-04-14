package eci.server.ItemModule.dto.member;

import eci.server.ItemModule.dto.item.ImageDto;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.member.ProfileImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String username;
    private String department;
    private String contact;
    private String profileImage;

    public static MemberDto toDto(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getDepartment(),
                member.getContact(),
                member.getProfileImage().getImageaddress()
        );
    }
}