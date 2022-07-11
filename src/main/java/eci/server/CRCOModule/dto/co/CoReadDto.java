package eci.server.CRCOModule.dto.co;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.cr.CrAttachmentDto;
import eci.server.CRCOModule.dto.cr.CrReadDto;
import eci.server.CRCOModule.dto.featuresdtos.*;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import eci.server.CRCOModule.entity.cofeatures.CoStage;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class CoReadDto {

    private Long id;

    private ClientOrganizationDto clientOrganizationId;
    private String clientItemNumber;

    private String coNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate coPublishPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate coReceivedPeriod;

    private String difference;

    private CarTypeDto carTypeId;

    private String costDifferent;

    private String costDifference;

    private CrReasonDto coReasonId;

    private CoStageDto coStageId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate applyPeriod;

    private List<CoEffectDto> coEffectId;

    private CrImportanceDto crImportanceId;

    private String name;
    private String content;

    private List<CrReadDto> relatedCr;

    private List<CoNewItemDto> affectedItemList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;

    private Boolean tempsave;
    private Boolean readonly;

    private Long routeId;

    private boolean preRejected;



    public static CoReadDto toDto(
            ChangeOrder co,
            RouteOrdering routeOrdering,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            BomRepository bomRepository,
            PreliminaryBomRepository preliminaryBomRepository,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ) {
        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByChangeOrder(co),
                        routeProductRepository,
                        routeOrderingRepository,
                        bomRepository,
                        preliminaryBomRepository,
                        defaultImageAddress
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new  CoReadDto(
                co.getId(),

                co.getClientOrganization()==null?
                        ClientOrganizationDto.toDto():
                        ClientOrganizationDto.toDto(co.getClientOrganization()),

                co.getClientItemNumber()==null||co.getClientItemNumber().isBlank()?" ":
                        co.getClientItemNumber(),

                co.getCoNumber(),

                co.getCoPublishPeriod()==null?null: (co.getCoPublishPeriod()),

                co.getCoReceivedPeriod()==null?null: (co.getCoReceivedPeriod()),

                co.getDifference()==true?"true":"false",

                co.getCarType()==null?
                        CarTypeDto.toDto():
                        CarTypeDto.toDto(co.getCarType()),

                co.getCostDifferent()==true?"true":"false",

                co.getCostDifference(),

                co.getCoReason()==null?
                CrReasonDto.toDto():
                        CrReasonDto.toDto(co.getCoReason()),

                co.getCoStage()==null?
                        CoStageDto.toDto():
                        CoStageDto.toDto(co.getCoStage()),

                co.getApplyPeriod()==null?null:co.getApplyPeriod(),

                co.getCoEffect()==null?CoEffectDto.toDtoList():
                        CoEffectDto.toDtoList(co.getCoEffect()),

                co.getCoImportance()==null?CrImportanceDto.toDto():
                        CrImportanceDto.toDto(co.getCoImportance()),

                co.getName(),

                co.getContent(),

                CrReadDto.toDtoList(
                        co.getChangeRequests(),
                        //routeOrdering,
                        routeOrderingRepository,
                        routeProductRepository,
                        attachmentTagRepository,
                        defaultImageAddress
                        ),

                CoNewItemDto.toDtoList(
                        co.getCoNewItems(),
                        routeOrderingRepository
                ),

                co.getCreatedAt(),
                MemberDto.toDto(co.getMember(),
                        defaultImageAddress),

                co.getModifier()==null?null:
                        co.getModifiedAt(),

                co.getModifier()==null?null:
                        MemberDto.toDto(co.getModifier(),
                                defaultImageAddress),

                co.getTempsave(),
                co.getReadonly(),

                routeOrderingRepository.findByChangeOrder(co).
                        get(routeOrderingRepository.findByChangeOrder(
                                co).size() - 1)
                        .getId(),

                CoPreRejected(routeOrdering ,routeProductRepository)

        );
    }


    public static CoReadDto noRouteDto(
            ChangeOrder co,
            //RouteOrdering routeOrdering,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ) {


        return new  CoReadDto(
                co.getId(),

                co.getClientOrganization()==null?
                        ClientOrganizationDto.toDto():
                        ClientOrganizationDto.toDto(co.getClientOrganization()),

                co.getClientItemNumber()==null||co.getClientItemNumber().isBlank()?" ":
                        co.getClientItemNumber(),

                co.getCoNumber(),

                co.getCoPublishPeriod()==null?null: (co.getCoPublishPeriod()),

                co.getCoReceivedPeriod()==null?null: (co.getCoReceivedPeriod()),

                co.getDifference()==true?"true":"false",

                co.getCarType()==null?
                        CarTypeDto.toDto():
                        CarTypeDto.toDto(co.getCarType()),

                co.getCostDifferent()==true?"true":"false",

                co.getCostDifference(),

                co.getCoReason()==null?
                        CrReasonDto.toDto():
                        CrReasonDto.toDto(co.getCoReason()),

                co.getCoStage()==null?
                        CoStageDto.toDto():
                        CoStageDto.toDto(co.getCoStage()),

                co.getApplyPeriod()==null?null:co.getApplyPeriod(),

                co.getCoEffect()==null?CoEffectDto.toDtoList():
                        CoEffectDto.toDtoList(co.getCoEffect()),

                co.getCoImportance()==null?CrImportanceDto.toDto():
                        CrImportanceDto.toDto(co.getCoImportance()),

                co.getName(),

                co.getContent(),

                CrReadDto.toDtoList(
                        co.getChangeRequests(),
                        //routeOrdering,
                        routeOrderingRepository,
                        routeProductRepository,
                        attachmentTagRepository,
                        defaultImageAddress
                ), //related cr

                CoNewItemDto.toDtoList(
                        co.getCoNewItems(),
                        routeOrderingRepository
                ),//affected ote,

                co.getCreatedAt(),
                MemberDto.toDto(co.getMember(),
                        defaultImageAddress),

                co.getModifier()==null?null:
                        co.getModifiedAt(),

                co.getModifier()==null?null:
                        MemberDto.toDto(co.getModifier(),
                                defaultImageAddress),

                co.getTempsave(),
                co.getReadonly(),

                -1L,

                false

        );
    }


    private static boolean CoPreRejected(RouteOrdering routeOrdering,
                                         RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());

                if (Objects.equals(currentRouteProduct.getType().getModule(), "CO") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "REQUEST")) {
                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }
}
