package eci.server.ItemModule.dto.member;

import eci.server.ItemModule.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String username;
    private String department;
    private String contact;

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getUsername(), member.getDepartment(), member.getContact());
    }
}