package eci.server.CRCOModule.dto.cr;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.featuresdtos.CrImportanceDto;
import eci.server.CRCOModule.dto.featuresdtos.CrReasonDto;
import eci.server.CRCOModule.dto.featuresdtos.CrSourceDto;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.ItemModule.dto.item.ItemProjectDto;
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
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrReadDto {

    private Long id;
    
    private String crNumber;

    private CrReasonDto crReasonDto;
    private CrSourceDto crSourceDto;
    private CrImportanceDto crImportanceDto;

    private List<CrAttachmentDto>  attachments;


    private String name;
    private String content;
    private String solution;
    
    private ItemProjectDto item;
    
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

    public static CrReadDto toDto(){
        return new CrReadDto();
    }

    public static  CrReadDto toDto(
            ChangeRequest changeRequest,
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
                        routeOrderingRepository.findByChangeRequest(changeRequest),
                        routeProductRepository,
                        routeOrderingRepository,
                        bomRepository,
                        preliminaryBomRepository,
                        defaultImageAddress
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new  CrReadDto(
                changeRequest.getId(),
                changeRequest.getCrNumber()==null?"":changeRequest.getCrNumber(),

                changeRequest.getCrReason()==null? CrReasonDto.toDto():CrReasonDto.toDto(changeRequest.getCrReason()),
                changeRequest.getCrSource()==null? CrSourceDto.toDto():CrSourceDto.toDto
                        (changeRequest.getCrSource()),
                changeRequest.getCrImportance()==null? CrImportanceDto.toDto():CrImportanceDto.toDto(
                        changeRequest.getCrImportance()),

                changeRequest.getAttachments().
                        stream().
                        map(i -> CrAttachmentDto.toDto(
                                i,
                                attachmentTagRepository))
                        .collect(toList()),

                changeRequest.getName(),
                changeRequest.getContent(),
                changeRequest.getSolution(),

                changeRequest.getNewItem()==null?
                        ItemProjectDto.toDto()
                        :ItemProjectDto.toDto(
                        changeRequest.getNewItem(),
                        routeOrderingRepository
                ),

                changeRequest.getCreatedAt(),
                MemberDto.toDto(changeRequest.getMember(),
                        defaultImageAddress),

                changeRequest.getModifier()==null?null:
                        changeRequest.getModifiedAt(),

                changeRequest.getModifier()==null?null:
                        MemberDto.toDto(changeRequest.getModifier(),
                                defaultImageAddress),

                changeRequest.isTempsave(),
                changeRequest.isReadonly(),

                routeOrderingRepository.findByChangeRequest(changeRequest).
                        get(routeOrderingRepository.findByChangeRequest(changeRequest)
                                .size() - 1)
                        .getId(),

                CrPreRejected(routeOrdering ,routeProductRepository)

        );
    }


    private static boolean CrPreRejected(RouteOrdering routeOrdering,
                                              RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());

            if (Objects.equals(currentRouteProduct.getType().getModule(), "PROJECT") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "CREATE")) {
                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }

    public static CrReadDto noRoutetoDto(
            ChangeRequest changeRequest,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress,
            RouteOrderingRepository routeOrderingRepository){

        return new CrReadDto(
                changeRequest.getId(),
                changeRequest.getCrNumber()==null?"":changeRequest.getCrNumber(),

                changeRequest.getCrReason()==null? CrReasonDto.toDto():CrReasonDto.toDto(changeRequest.getCrReason()),
                changeRequest.getCrSource()==null? CrSourceDto.toDto():CrSourceDto.toDto
                        (changeRequest.getCrSource()),
                changeRequest.getCrImportance()==null? CrImportanceDto.toDto():CrImportanceDto.toDto(
                        changeRequest.getCrImportance()),

                changeRequest.getAttachments().
                        stream().
                        map(i -> CrAttachmentDto.toDto(
                                i,
                                attachmentTagRepository))
                        .collect(toList()),

                changeRequest.getName(),
                changeRequest.getContent(),
                changeRequest.getSolution(),

                ItemProjectDto.noRoutetoDto(
                        changeRequest.getNewItem(),
                        routeOrderingRepository
                ),

                changeRequest.getCreatedAt(),
                MemberDto.toDto(changeRequest.getMember(),
                        defaultImageAddress),

                changeRequest.getModifier()==null?null:
                        changeRequest.getModifiedAt(),

                changeRequest.getModifier()==null?null:
                        MemberDto.toDto(changeRequest.getModifier(),
                                defaultImageAddress),

                changeRequest.isTempsave(),
                changeRequest.isReadonly(),

                -1L,

                false

        );

}

/////dto list

    public static  List<CrReadDto> toDtoList(
            List<ChangeRequest> changeRequests,
            //RouteOrdering routeOrdering,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ) {


        List<CrReadDto> crList = changeRequests.stream().map(
                changeRequest -> new CrReadDto(
                        changeRequest.getId(),
                        changeRequest.getCrNumber() == null ? "" : changeRequest.getCrNumber(),

                        changeRequest.getCrReason() == null ? CrReasonDto.toDto() : CrReasonDto.toDto(changeRequest.getCrReason()),
                        changeRequest.getCrSource() == null ? CrSourceDto.toDto() : CrSourceDto.toDto
                                (changeRequest.getCrSource()),
                        changeRequest.getCrImportance() == null ? CrImportanceDto.toDto() : CrImportanceDto.toDto(
                                changeRequest.getCrImportance()),

                        changeRequest.getAttachments().
                                stream().
                                map(i -> CrAttachmentDto.toDto(
                                        i,
                                        attachmentTagRepository))
                                .collect(toList()),

                        changeRequest.getName(),
                        changeRequest.getContent(),
                        changeRequest.getSolution(),

                        changeRequest.getNewItem() == null ?
                                ItemProjectDto.toDto()
                                : ItemProjectDto.toDto(
                                changeRequest.getNewItem(),
                                routeOrderingRepository
                        ),

                        changeRequest.getCreatedAt(),
                        MemberDto.toDto(changeRequest.getMember(),
                                defaultImageAddress),

                        changeRequest.getModifier() == null ? null :
                                changeRequest.getModifiedAt(),

                        changeRequest.getModifier() == null ? null :
                                MemberDto.toDto(changeRequest.getModifier(),
                                        defaultImageAddress),

                        changeRequest.isTempsave(),
                        changeRequest.isReadonly(),

                        routeOrderingRepository.findByChangeRequest(changeRequest).size()>0?
                        routeOrderingRepository.findByChangeRequest(changeRequest).
                                get(routeOrderingRepository.findByChangeRequest(changeRequest)
                                        .size() - 1)
                                .getId()
                        :-1L,

                        //CrPreRejected(routeOrdering, routeProductRepository)
                        false
                )

        ).collect(toList());

        return crList;
    }
    //
    public static  CrReadDto toDto(
            ChangeRequest changeRequest
    ) {

        return new  CrReadDto(
                changeRequest.getId(),
                changeRequest.getCrNumber()==null?"":changeRequest.getCrNumber(),

                CrReasonDto.toDto(),
                CrSourceDto.toDto(),
                CrImportanceDto.toDto(),

                new ArrayList<>(),

                " ",
                " ",
                " ",

                        ItemProjectDto.toDto(),

                null,

                MemberDto.toDto(),

                null,
                MemberDto.toDto(),

                changeRequest.isTempsave(),
                changeRequest.isReadonly(),

                -1L,
                false

        );
    }



}


