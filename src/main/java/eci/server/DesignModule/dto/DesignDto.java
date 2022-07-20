package eci.server.DesignModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.cr.CrAttachmentDto;
import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.dto.item.ItemDesignDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;


    private Boolean tempsave;
    private boolean readonly;

    private String designContent; //단순 시연용

    private boolean preRejected;

    public static DesignDto toDto(
            RouteOrdering routeOrdering,
            Design design,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            BomRepository bomRepository,
            PreliminaryBomRepository preliminaryBomRepository,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ) {

        List<DesignAttachmentDto> attachmentDtoList = new ArrayList<>();
        if(design.getDesignAttachments()!=null) {
            attachmentDtoList
                    .addAll(design.getDesignAttachments().
                            stream().
                            map(i ->
                                    DesignAttachmentDto.toDto
                                            (i, attachmentTagRepository)
                            )
                            .collect(toList()));

            Collections.sort(attachmentDtoList);
        }

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()),
                        routeProductRepository,
                        routeOrderingRepository,
                        defaultImageAddress
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new DesignDto(

                design.getId(),
                ItemDesignDto.toDto(design.getNewItem(), defaultImageAddress),
                MemberDto.toDto(design.getMember(), defaultImageAddress),

                attachmentDtoList,

                //routeDtoList

                //가장 최신의 라우트 오더링 중 최신의 라우트 오더링 아이디
                routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).
                        get(routeOrderingRepository.findByNewItemOrderByIdAsc(design.getNewItem()).size() - 1)
                        .getId(),

                design.getCreatedAt(),
                MemberDto.toDto(design.getMember(), defaultImageAddress),

                design.getModifier()==null?null:design.getModifiedAt(),
                design.getModifier()==null?null:MemberDto.toDto(design.getModifier(), defaultImageAddress),

                design.getTempsave(),
                design.getReadonly(),

                design.getDesignContent(), //단순 시연용

                DesignPreRejected(routeOrdering, routeProductRepository)
        );
    }


    private static boolean DesignPreRejected(RouteOrdering routeOrdering,
                                              RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());

            if (Objects.equals(currentRouteProduct.getType().getModule(), "DESIGN") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "CREATE")) {
                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }

}
