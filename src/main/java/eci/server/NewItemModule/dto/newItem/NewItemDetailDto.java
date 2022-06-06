package eci.server.NewItemModule.dto.newItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.dto.color.ColorDto;
import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.NewItemModule.dto.ItemTypesDto;
import eci.server.NewItemModule.dto.attachment.NewItemAttachmentDto;
import eci.server.NewItemModule.dto.classification.ClassificationDto;
import eci.server.NewItemModule.dto.coatingcommon.CoatingDto;
import eci.server.NewItemModule.dto.image.NewItemImageDto;
import eci.server.NewItemModule.dto.supplier.SupplierDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.repository.maker.NewItemMakerRepository;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class NewItemDetailDto {

    private Long id;
    private ClassificationDto classification;
    private String name;
    private ItemTypesDto itemTypes;
    private String itemNumber;
    private NewItemImageDto thumbnail;
    private boolean sharing;
    private CarTypeDto carTypeId;

    private String integrate;
    private String curve;
    private String width;
    private String height;
    private String thickness;
    private String weight;
    private String importance;
    private ColorDto colorId;
    private String loadQuantity;
    private String forming;
    private CoatingDto coatingWayId;
    private CoatingDto coatingTypeId;
    private String modulus;
    private String screw;
    private String cuttingType;
    private String lcd;
    private String displaySize;
    private String screwHeight;
    private ClientOrganizationDto clientOrganizationId;
    private SupplierDto supplierOrganizationId;
    private List<MakerSimpleDto> makersId;
    private String partnumbers;
    private boolean revise_progress;

    private List<NewItemAttachmentDto> attachments;
    //아래는 읽기 전용 속성
    private char revision;
    private String status;

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


    public static NewItemDetailDto toDto(
            NewItem Item,
            NewItemMakerRepository newItemMakerRepository,
            RouteOrderingDto routeOrderingDto,
            RouteProductDto routeProductDto
    ) {

        if(Item.getMakers().size()>0) {
            return new NewItemDetailDto(
                    Item.getId(),
                    ClassificationDto.toDto(Item.getClassification()),
                    Item.getName(),
                    ItemTypesDto.toDto(Item.getItemTypes()),
                    Item.getItemNumber(),
                    NewItemImageDto.toDto(Item.getThumbnail()),
                    Item.isSharing(),
                    CarTypeDto.toDto(Item.getCarType()),
                    Item.getIntegrate(),
                    Item.getCurve(),
                    Item.getWidth(),
                    Item.getHeight(),
                    Item.getThickness(),
                    Item.getWeight(),
                    Item.getImportance(),
                    ColorDto.toDto(Item.getColor()),
                    Item.getLoadQuantity(),
                    Item.getForming(),
                    CoatingDto.toDto(Item.getCoatingWay()),
                    CoatingDto.toDto(Item.getCoatingType()),
                    Item.getModulus(),
                    Item.getScrew(),
                    Item.getCuttingType(),
                    Item.getLcd(),
                    Item.getDisplaySize(),
                    Item.getScrewHeight(),
                    ClientOrganizationDto.toDto(Item.getClientOrganization()),
                    SupplierDto.toDto(Item.getSupplierOrganization()),
                    MakerSimpleDto.toDtoList(Item.getMakers()),
                    newItemMakerRepository.findByMaker(Item.getMakers().get(0).getMaker()).get(0).getPartnumber(),
                    Item.isRevise_progress(),

                    Item.getAttachments().
                            stream().
                            map(NewItemAttachmentDto::toDto)
                            .collect(toList()),
                    (char) Item.getRevision(),
                    routeOrderingDto.getLifecycleStatus(),

                    routeOrderingDto.getId(),
                    Item.getCreatedAt(),
                    MemberDto.toDto(Item.getMember()),

                    Item.getModifier()==null?null:Item.getModifiedAt(),
                    Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier()),

                    Item.isTempsave(),
                    Item.isReadonly()


            );
        }
        return new NewItemDetailDto(
                Item.getId(),
                ClassificationDto.toDto(Item.getClassification()),
                Item.getName(),
                ItemTypesDto.toDto(Item.getItemTypes()),
                Item.getItemNumber(),
                NewItemImageDto.toDto(Item.getThumbnail()),
                Item.isSharing(),
                CarTypeDto.toDto(Item.getCarType()),
                Item.getIntegrate(),
                Item.getCurve(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getThickness(),
                Item.getWeight(),
                Item.getImportance(),
                ColorDto.toDto(Item.getColor()),
                Item.getLoadQuantity(),
                Item.getForming(),
                CoatingDto.toDto(Item.getCoatingWay()),
                CoatingDto.toDto(Item.getCoatingType()),
                Item.getModulus(),
                Item.getScrew(),
                Item.getCuttingType(),
                Item.getLcd(),
                Item.getDisplaySize(),
                Item.getScrewHeight(),
                ClientOrganizationDto.toDto(Item.getClientOrganization()),
                SupplierDto.toDto(Item.getSupplierOrganization()),
                MakerSimpleDto.toDtoList(Item.getMakers()),
                "",

                Item.isRevise_progress(),

                Item.getAttachments().
                        stream().
                        map(NewItemAttachmentDto::toDto)
                        .collect(toList()),

                (char) Item.getRevision(),
                routeOrderingDto.getLifecycleStatus(),
                routeOrderingDto.getId(),
                Item.getCreatedAt(),
                MemberDto.toDto(Item.getMember()),

                Item.getModifier()==null?null:Item.getModifiedAt(),
                Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier()),

                Item.isTempsave(),
                Item.isReadonly()

        );
    }


    public static NewItemDetailDto noRoutetoDto(
            NewItem Item,
            NewItemMakerRepository newItemMakerRepository
    ){
        if(Item.getMakers().size()>0) {
            return new NewItemDetailDto(
                    Item.getId(),
                    ClassificationDto.toDto(Item.getClassification()),
                    Item.getName(),
                    ItemTypesDto.toDto(Item.getItemTypes()),
                    Item.getItemNumber(),
                    NewItemImageDto.toDto(Item.getThumbnail()),
                    Item.isSharing(),
                    CarTypeDto.toDto(Item.getCarType()),
                    Item.getIntegrate(),
                    Item.getCurve(),
                    Item.getWidth(),
                    Item.getHeight(),
                    Item.getThickness(),
                    Item.getWeight(),
                    Item.getImportance(),
                    ColorDto.toDto(Item.getColor()),
                    Item.getLoadQuantity(),
                    Item.getForming(),
                    CoatingDto.toDto(Item.getCoatingWay()),
                    CoatingDto.toDto(Item.getCoatingType()),
                    Item.getModulus(),
                    Item.getScrew(),
                    Item.getCuttingType(),
                    Item.getLcd(),
                    Item.getDisplaySize(),
                    Item.getScrewHeight(),
                    ClientOrganizationDto.toDto(Item.getClientOrganization()),
                    SupplierDto.toDto(Item.getSupplierOrganization()),
                    MakerSimpleDto.toDtoList(Item.getMakers()),
                    newItemMakerRepository.findByMaker(Item.getMakers().get(0).getMaker()).get(0).getPartnumber(),
                    Item.isRevise_progress(),

                    Item.getAttachments().
                            stream().
                            map(NewItemAttachmentDto::toDto)
                            .collect(toList()),
                    (char) Item.getRevision(),
                    "LIFECYCLE_NONE",
                    0L,
                    Item.getCreatedAt(),
                    MemberDto.toDto(Item.getMember()),

                    Item.getModifier()==null?null:Item.getModifiedAt(),
                    Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier()),

                    Item.isTempsave(),
                    Item.isReadonly()


            );
        }
        return new NewItemDetailDto(
                Item.getId(),
                ClassificationDto.toDto(Item.getClassification()),
                Item.getName(),
                ItemTypesDto.toDto(Item.getItemTypes()),
                Item.getItemNumber(),
                NewItemImageDto.toDto(Item.getThumbnail()),
                Item.isSharing(),
                CarTypeDto.toDto(Item.getCarType()),
                Item.getIntegrate(),
                Item.getCurve(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getThickness(),
                Item.getWeight(),
                Item.getImportance(),
                ColorDto.toDto(Item.getColor()),
                Item.getLoadQuantity(),
                Item.getForming(),
                CoatingDto.toDto(Item.getCoatingWay()),
                CoatingDto.toDto(Item.getCoatingType()),
                Item.getModulus(),
                Item.getScrew(),
                Item.getCuttingType(),
                Item.getLcd(),
                Item.getDisplaySize(),
                Item.getScrewHeight(),
                ClientOrganizationDto.toDto(Item.getClientOrganization()),
                SupplierDto.toDto(Item.getSupplierOrganization()),
                MakerSimpleDto.toDtoList(Item.getMakers()),
                "no partnum",
                Item.isRevise_progress(),

                Item.getAttachments().
                        stream().
                        map(NewItemAttachmentDto::toDto)
                        .collect(toList()),

                (char) Item.getRevision(),
                "LIFECYCLE_NONE",
                0L,
                Item.getCreatedAt(),
                MemberDto.toDto(Item.getMember()),

                Item.getModifier()==null?null:Item.getModifiedAt(),
                Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier()),

                Item.isTempsave(),
                Item.isReadonly()

        );
    }

}