package eci.server.DesignModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.dto.item.ItemDesignDto;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
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

    private ItemDesignDto item;
    private MemberDto member;

    private List<DesignAttachmentDto> designAttachments;

    private Long routeId;

    //05-22 추가
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;


    private Boolean tempsave;
    private boolean readonly;


    public static DesignDto toDto(
            Design design,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            BomRepository bomRepository,
            PreliminaryBomRepository preliminaryBomRepository
    ) {
        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByNewItem(design.getNewItem()),
                        routeProductRepository,
                        routeOrderingRepository,
                        bomRepository,
                        preliminaryBomRepository
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new DesignDto(

                design.getId(),
                ItemDesignDto.toDto(design.getNewItem()),
                MemberDto.toDto(design.getMember()),

                design.getDesignAttachments().
                        stream().
                        map(i -> DesignAttachmentDto.toDto(i))
                        .collect(toList()),

                //routeDtoList

                //가장 최신의 라우트 오더링 중 최신의 라우트 오더링 아이디
                routeOrderingRepository.findByNewItem(design.getNewItem()).
                        get(routeOrderingRepository.findByNewItem(design.getNewItem()).size() - 1)
                        .getId(),

                design.getCreatedAt(),
                MemberDto.toDto(design.getMember()),

                design.getModifier()==null?null:design.getModifiedAt(),
                design.getModifier()==null?null:MemberDto.toDto(design.getModifier()),

                design.getTempsave(),
                design.getReadonly()


        );
    }
}
