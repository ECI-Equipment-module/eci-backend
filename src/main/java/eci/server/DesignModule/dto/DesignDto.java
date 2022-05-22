package eci.server.DesignModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.RouteOrderingDto;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class DesignDto {
    private Long id;

    private ItemProjectDto item;
    private MemberDto member;
    private Boolean tempsave;
    private List<DesignAttachmentDto> designAttachments;

    private Long routeId;

    //05-22 추가
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;


    public static DesignDto toDto(
            Design design,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository
    ) {
        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByItem(design.getItem()),
                        routeProductRepository,
                        routeOrderingRepository
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new DesignDto(

                design.getId(),
                ItemProjectDto.toDto(design.getItem()),
                MemberDto.toDto(design.getMember()),
                design.getTempsave(),

                design.getDesignAttachments().
                        stream().
                        map(i -> DesignAttachmentDto.toDto(i))
                        .collect(toList()),

                //routeDtoList

                //가장 최신의 라우트 오더링 중 최신의 라우트 오더링 아이디
                routeOrderingRepository.findByItem(design.getItem()).
                        get(routeOrderingRepository.findByItem(design.getItem()).size() - 1)
                        .getId(),

                design.getCreatedAt(),
                MemberDto.toDto(design.getMember()),

                design.getModifier()==null?null:design.getModifiedAt(),
                design.getModifier()==null?null:MemberDto.toDto(design.getModifier())


        );
    }
}
