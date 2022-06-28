package eci.server.ItemModule.dto.member;

import eci.server.ItemModule.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public static MemberDto toDto(Member member,
                                  String defaultImageAddress) {



        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getDepartment(),
                member.getContact(),
                member.getProfileImage()==null?
                        defaultImageAddress :
                        member.getProfileImage().getImageaddress()
        );
    }

    public static List <MemberDto> toDtoList(
            List<Member> members
    ) {

        List<MemberDto> memberDtos = members.stream().map(
                member -> new MemberDto(
                        member.getId(),
                        member.getEmail(),
                        member.getUsername(),
                        member.getDepartment(),
                        member.getContact(),
                        member.getProfileImage().getImageaddress()
                )
        ).collect(
                toList()
        );
        return memberDtos;
    }
}