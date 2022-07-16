package eci.server.ReleaseModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.co.CoSearchDto;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationDto;
import eci.server.ReleaseModule.dto.type.ReleaseTypeDto;
import eci.server.ReleaseModule.entity.Releasing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 *
 */
public class ReleaseDto{
    private Long id;

    private String releaseNumber;

    private ReleaseTypeDto releaseType;

    private String releaseTitle;

    private String itemNumber;

    private String coNumber;

    //////////////////////////////////////////

    private String releaseContent;

    private MemberDto member;

    private List<ReleaseAttachmentDto> projectAttachments;

    private Long routeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;

    private Boolean tempsave;
    private boolean readonly;

    private boolean preRejected;

    private List<ProduceOrganizationDto> releaseOrganizationId;

    @Nullable
    private ItemProjectDto item;

    @Nullable
    private CoSearchDto releaseCoId;


    public static ReleaseDto toDto(
            Releasing release,
            RouteOrdering routeOrdering,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository,
            BomRepository bomRepository,
            PreliminaryBomRepository preliminaryBomRepository,
            String defaultImageAddress
    ) {


        return new ReleaseDto(

                release.getId(),

                release.getReleaseNumber(),

                release.getReleaseType()==null?ReleaseTypeDto.toDto()
                        :ReleaseTypeDto.toDto(release.getReleaseType()),

                release.getReleaseTitle(),

                release.getNewItem()==null?
                        " " : release.getNewItem().getItemNumber(),

                release.getChangeOrder()==null?
                        " " : release.getChangeOrder().getCoNumber(),

                release.getReleaseContent(),

                MemberDto.toDto(release.getMember(),defaultImageAddress),

                release.getAttachments().
                        stream().
                        map(i -> ReleaseAttachmentDto.toDto(
                                i,
                                attachmentTagRepository))
                        .collect(toList()),

                //가장 최신의 라우트 오더링 중 최신의 라우트 오더링 아이디
                routeOrderingRepository.findByReleaseOrderByIdAsc(release).size()>0?
                routeOrderingRepository.findByReleaseOrderByIdAsc(release).
                        get(routeOrderingRepository.findByReleaseOrderByIdAsc(release).size() - 1)
                        .getId()
                : //라우트 없는 경우엔
                        -1L ,

                //05-22추가
                release.getCreatedAt(),
                MemberDto.toDto(release.getMember(), defaultImageAddress),

                release.getModifier()==null?null:release.getModifiedAt(),
                release.getModifier()==null?null:MemberDto.toDto(release.getModifier(), defaultImageAddress),

                release.getTempsave(),
                release.getReadonly(),

                ReleasePreRejected(routeOrdering ,routeProductRepository),

                ProduceOrganizationDto.toDtoList(
                        release.getReleaseOrganization()
                ),

                release.getNewItem()==null?
                        ItemProjectDto.toDto()
                        :
                        ItemProjectDto.toDto(release.getNewItem(),routeOrderingRepository),

                release.getChangeOrder()==null?
                        CoSearchDto.toDto():
                        CoSearchDto.toDto(
                                release.getChangeOrder()
                        )


        );
    }

    public static ReleaseDto noRoutetoDto(
            Releasing release,
            //RouteOrdering routeOrdering,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository,
            BomRepository bomRepository,
            PreliminaryBomRepository preliminaryBomRepository,
            String defaultImageAddress
    ) {

        return new ReleaseDto(

                release.getId(),

                release.getReleaseNumber(),

                release.getReleaseType()==null?ReleaseTypeDto.toDto()
                        :ReleaseTypeDto.toDto(release.getReleaseType()),

                release.getReleaseTitle(),

                release.getNewItem()==null?
                        " " : release.getNewItem().getItemNumber(),

                release.getChangeOrder()==null?
                        " " : release.getChangeOrder().getCoNumber(),

                release.getReleaseContent(),

                MemberDto.toDto(release.getMember(),defaultImageAddress),

                release.getAttachments().
                        stream().
                        map(i -> ReleaseAttachmentDto.toDto(
                                i,
                                attachmentTagRepository))
                        .collect(toList()),

                -1L ,

                //05-22추가
                release.getCreatedAt(),
                MemberDto.toDto(release.getMember(), defaultImageAddress),

                release.getModifier()==null?null:release.getModifiedAt(),
                release.getModifier()==null?null:MemberDto.toDto(release.getModifier(), defaultImageAddress),

                release.getTempsave(),
                release.getReadonly(),

                false,

                ProduceOrganizationDto.toDtoList(
                        release.getReleaseOrganization()
                ),

                release.getNewItem()==null?
                        ItemProjectDto.toDto()
                        :
                        ItemProjectDto.toDto(release.getNewItem(),routeOrderingRepository),

                release.getChangeOrder()==null?
                        CoSearchDto.toDto():
                        CoSearchDto.toDto(
                                release.getChangeOrder()
                        )


        );
    }


    public static List<ReleaseDto> toDtoList(
            List<Releasing> releaseList,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ) {
        List<ReleaseDto> releaseDtos = releaseList.stream().map(

                release -> new ReleaseDto(

                release.getId(),

                release.getReleaseNumber(),

                release.getReleaseType()==null?ReleaseTypeDto.toDto()
                        :ReleaseTypeDto.toDto(release.getReleaseType()),

                release.getReleaseTitle(),

                release.getNewItem()==null?
                        " " : release.getNewItem().getItemNumber(),

                release.getChangeOrder()==null?
                        " " : release.getChangeOrder().getCoNumber(),

                release.getReleaseContent(),

                MemberDto.toDto(release.getMember(),defaultImageAddress),

                release.getAttachments().
                        stream().
                        map(i -> ReleaseAttachmentDto.toDto(
                                i,
                                attachmentTagRepository))
                        .collect(toList()),

                //가장 최신의 라우트 오더링 중 최신의 라우트 오더링 아이디
                routeOrderingRepository.findByReleaseOrderByIdAsc(release).size()>0?
                        routeOrderingRepository.findByReleaseOrderByIdAsc(release).
                                get(routeOrderingRepository.findByReleaseOrderByIdAsc(release).size() - 1)
                                .getId()
                        : //라우트 없는 경우엔
                        -1L ,

                //05-22추가
                release.getCreatedAt(),
                MemberDto.toDto(release.getMember(), defaultImageAddress),

                release.getModifier()==null?null:release.getModifiedAt(),
                release.getModifier()==null?null:MemberDto.toDto(release.getModifier(), defaultImageAddress),

                release.getTempsave(),
                release.getReadonly(),

                ReleasePreRejected(
                        routeOrderingRepository.findByReleaseOrderByIdAsc(release).size()>0?
                        routeOrderingRepository.findByReleaseOrderByIdAsc(release).
                                get(routeOrderingRepository.findByReleaseOrderByIdAsc(release).size() - 1)
                                :null

                        ,routeProductRepository),

                ProduceOrganizationDto.toDtoList(
                        release.getReleaseOrganization()
                ),

                release.getNewItem()==null?
                        ItemProjectDto.toDto()
                        :
                        ItemProjectDto.toDto(release.getNewItem(),routeOrderingRepository),

                release.getChangeOrder()==null?
                        CoSearchDto.toDto():
                        CoSearchDto.toDto(
                                release.getChangeOrder()
                        )


        )

        ).collect(toList());

        return releaseDtos;
    }


    private static boolean ReleasePreRejected(RouteOrdering routeOrdering,
                                              RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());

                if (Objects.equals(currentRouteProduct.getType().getModule(), "RELEASE") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "CREATE")) {
                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }
}

