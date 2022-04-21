package eci.server.ItemModule.dto.member;

import eci.server.ItemModule.dto.item.ImageDto;
import eci.server.ItemModule.dto.newRoute.NewRouteDto;
import eci.server.ItemModule.dto.newRoute.RouteProductDto;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.member.ProfileImage;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

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