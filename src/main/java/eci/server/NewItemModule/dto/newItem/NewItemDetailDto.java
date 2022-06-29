package eci.server.NewItemModule.dto.newItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.BomModule.repository.BomRepository;
import eci.server.DesignModule.dto.itemdesign.BomDesignItemDto;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.color.ColorDto;
import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.dto.ItemTypesDto;
import eci.server.NewItemModule.dto.attachment.NewItemAttachmentDto;
import eci.server.NewItemModule.dto.classification.ClassificationDto;
import eci.server.NewItemModule.dto.coatingcommon.CoatingDto;
import eci.server.NewItemModule.dto.image.NewItemImageDto;
import eci.server.NewItemModule.dto.responsibility.DesignResponsibleDto;
import eci.server.NewItemModule.dto.supplier.SupplierDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.maker.MakerRepository;
import eci.server.NewItemModule.repository.maker.NewItemMakerRepository;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationDto;
import eci.server.config.guard.BomGuard;
import eci.server.config.guard.DesignGuard;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.RouteMatcher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.partitioningBy;
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
//    private List<MakerSimpleDto> makersId;
    private MakerSimpleDto makersId;
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
    private boolean preRejected;

    //private BomDesignItemDto bom;
    private Long bomId;
    private BomDesignItemDto design;


    public static NewItemDetailDto toDto(
            NewItem Item,
            RouteOrdering routeOrdering,
            RouteOrderingDto routeOrderingDto,
            DesignRepository designRepository,
            BomRepository bomRepository,
            RouteProductRepository routeProductRepository,
            DesignGuard designGuard,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ) {

        List<DesignResponsibleDto> tmpResponsibleDtoList = new ArrayList<>();

        NewItemImageDto nullImage = new NewItemImageDto(defaultImageAddress);
        ClassificationDto nullClassification = new ClassificationDto();
        ItemTypesDto nullItemTypesDto = new ItemTypesDto();
        CarTypeDto nullCarTypeDto = new CarTypeDto();

        if(Item.getMakers()!=null) {
            return new NewItemDetailDto(
                    Item.getId(),

                    Item.getClassification()==null?
                            nullClassification
                    :ClassificationDto.toDto(Item.getClassification()),

                    Item.getName(),

                    Item.getItemTypes() == null?
                            nullItemTypesDto:
                            ItemTypesDto.toDto(Item.getItemTypes()),

                    Item.getItemNumber(),

                    Item.getThumbnail()==null?
                            nullImage:
                            NewItemImageDto.toDto(Item.getThumbnail()),

                    Item.isSharing(),

                    Item.getCarType()==null?
                            nullCarTypeDto:
                    CarTypeDto.toDto(Item.getCarType()),

                    Item.getIntegrate(),
                    Item.getCurve(),
                    Item.getWidth(),
                    Item.getHeight(),
                    Item.getThickness(),
                    Item.getWeight(),
                    Item.getImportance(),

                    Item.getColor()==null?
                            ColorDto.toDto():
                    ColorDto.toDto(Item.getColor()),
                    Item.getLoadQuantity(),
                    Item.getForming(),

                    Item.getCoatingWay()==null?
                            CoatingDto.toDto():
                    CoatingDto.toDto(Item.getCoatingWay()),
                    Item.getCoatingType()==null?
                            CoatingDto.toDto():
                    CoatingDto.toDto(Item.getCoatingType()),
                    Item.getModulus(),
                    Item.getScrew(),
                    Item.getCuttingType(),
                    Item.getLcd(),
                    Item.getDisplaySize(),
                    Item.getScrewHeight(),

                    Item.getClientOrganization()==null?ClientOrganizationDto.toDto():
                    ClientOrganizationDto.toDto(Item.getClientOrganization()),

                    Item.getSupplierOrganization()==null?SupplierDto.toDto():
                    SupplierDto.toDto(Item.getSupplierOrganization()),

                    Item.getMakers()==null?MakerSimpleDto.toDto():
                            MakerSimpleDto.toDto(Item.getMakers()),

                    Item.getPartNumber(),
                    //MakerSimpleDto.toDtoList(Item.getMakers()),
                            //MakerSimpleDto.toDtoList(Item.getMakers()),


                    //newItemMakerRepository.findByMaker(Item.getMakers().get(0).getMaker()).get(0).getPartnumber(),
                    Item.isRevise_progress(),

                    Item.getAttachments().
                            stream().
                            map( i->
                                    NewItemAttachmentDto.toDto
                                            (i,attachmentTagRepository)
                            )
                            .collect(toList()),
                    (char) Item.getRevision(),
                    routeOrderingDto.getLifecycleStatus(),

                    routeOrderingDto.getId(),
                    Item.getCreatedAt(),
                    MemberDto.toDto(Item.getMember(), defaultImageAddress),

                    Item.getModifier()==null?null:Item.getModifiedAt(),
                    Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier(), defaultImageAddress),

                    Item.isTempsave(),
                    Item.isReadonly(),

                    ItemPreRejected(routeOrdering,routeProductRepository),

                    bomRepository.findByNewItem(Item).size()>0?
                            bomRepository.findByNewItem(Item).get(0).getId()
                            :-1L,

                    designRepository.findByNewItem(Item).size()>0?
                            BomDesignItemDto.toDesignDto(
                                    Item,
                                    designRepository.findByNewItem(Item).get(
                                            designRepository.findByNewItem(Item).size() - 1
                                    ),
                                    designGuard
                            ) :
                            //디자인 없을 지도 모름 - 0613 수정
                            BomDesignItemDto.toColorDesignDto(
                                    Item,
                                    designGuard
                            )


            );
        }
        return new NewItemDetailDto(
                Item.getId(),

                Item.getClassification()==null?
                        nullClassification
                        :ClassificationDto.toDto(Item.getClassification()),

                Item.getName(),

                Item.getItemTypes() == null?
                        nullItemTypesDto:
                        ItemTypesDto.toDto(Item.getItemTypes()),

                Item.getItemNumber(),

                Item.getThumbnail()==null?
                        nullImage:
                        NewItemImageDto.toDto(Item.getThumbnail()),

                Item.isSharing(),

                Item.getCarType()==null?
                        nullCarTypeDto:
                        CarTypeDto.toDto(Item.getCarType()),

                Item.getIntegrate(),
                Item.getCurve(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getThickness(),
                Item.getWeight(),
                Item.getImportance(),

                Item.getColor()==null?
                        ColorDto.toDto():
                        ColorDto.toDto(Item.getColor()),
                Item.getLoadQuantity(),
                Item.getForming(),

                Item.getCoatingWay()==null?
                        CoatingDto.toDto():
                        CoatingDto.toDto(Item.getCoatingWay()),
                Item.getCoatingType()==null?
                        CoatingDto.toDto():
                        CoatingDto.toDto(Item.getCoatingType()),
                Item.getModulus(),
                Item.getScrew(),
                Item.getCuttingType(),
                Item.getLcd(),
                Item.getDisplaySize(),
                Item.getScrewHeight(),

                Item.getClientOrganization()==null?ClientOrganizationDto.toDto():
                        ClientOrganizationDto.toDto(Item.getClientOrganization()),

                Item.getSupplierOrganization()==null?SupplierDto.toDto():
                        SupplierDto.toDto(Item.getSupplierOrganization()),

                Item.getMakers() ==null?
                        MakerSimpleDto.toDto():
                MakerSimpleDto.toDto(Item.getMakers()),
                "",


                Item.isRevise_progress(),

                Item.getAttachments().
                        stream().
                        map( i->
                                NewItemAttachmentDto.toDto
                                        (i,attachmentTagRepository)
                        )
                        .collect(toList()),

                (char) Item.getRevision(),
                routeOrderingDto.getLifecycleStatus(),
                routeOrderingDto.getId(),
                Item.getCreatedAt(),
                MemberDto.toDto(Item.getMember(), defaultImageAddress),

                Item.getModifier()==null?null:Item.getModifiedAt(),
                Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier(), defaultImageAddress),

                Item.isTempsave(),
                Item.isReadonly(),

                ItemPreRejected(routeOrdering,routeProductRepository),

//                BomDesignItemDto.toBomDto(
//                        Item,
//                        bomRepository.findByNewItem(Item).get(
//                                designRepository.findByNewItem(Item).size()-1
//                        ),
//                        bomGuard
//                ),

                bomRepository.findByNewItem(Item).size()>0?
                        bomRepository.findByNewItem(Item).get(0).getId()
                        :-1L,

                designRepository.findByNewItem(Item).size()>0?
                        BomDesignItemDto.toDesignDto(
                                Item,
                                designRepository.findByNewItem(Item).get(
                                        designRepository.findByNewItem(Item).size() - 1
                                ),
                                designGuard
                        ) :
                        //디자인 없을 지도 모름
                        BomDesignItemDto.toColorDesignDto(
                                Item,
                                designGuard
                        )

        );
    }


    public static NewItemDetailDto noRoutetoDto( //edit 창인 애들 불러올 때
            NewItem Item,
            //NewItemMakerRepository newItemMakerRepository,
            RouteOrdering routeOrdering,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository,
            String defaultImageAddress
    ){
        NewItemImageDto nullImage = new NewItemImageDto(defaultImageAddress);
        ClassificationDto nullClassification = new ClassificationDto();
        ItemTypesDto nullItemTypesDto = new ItemTypesDto();
        CarTypeDto nullCarTypeDto = new CarTypeDto();

        List<DesignResponsibleDto> tmpResponsibleDtoList = new ArrayList<>();

        if(Item.getMakers()!=null) {
            return new NewItemDetailDto(
                    Item.getId(),

                    Item.getClassification()==null?
                            nullClassification
                            :ClassificationDto.toDto(Item.getClassification()),

                    Item.getName(),

                    Item.getItemTypes() == null?
                            nullItemTypesDto:
                            ItemTypesDto.toDto(Item.getItemTypes()),

                    Item.getItemNumber(),
                    Item.getThumbnail()==null?
                            nullImage:
                            NewItemImageDto.toDto(Item.getThumbnail()),
                    Item.isSharing(),

                    Item.getCarType()==null?
                            nullCarTypeDto:
                            CarTypeDto.toDto(Item.getCarType()),

                    Item.getIntegrate(),
                    Item.getCurve(),
                    Item.getWidth(),
                    Item.getHeight(),
                    Item.getThickness(),
                    Item.getWeight(),
                    Item.getImportance(),

                    Item.getColor()==null?
                            ColorDto.toDto():
                            ColorDto.toDto(Item.getColor()),
                    Item.getLoadQuantity(),
                    Item.getForming(),

                    Item.getCoatingWay()==null?
                            CoatingDto.toDto():
                            CoatingDto.toDto(Item.getCoatingWay()),
                    Item.getCoatingType()==null?
                            CoatingDto.toDto():
                            CoatingDto.toDto(Item.getCoatingType()),
                    Item.getModulus(),
                    Item.getScrew(),
                    Item.getCuttingType(),
                    Item.getLcd(),
                    Item.getDisplaySize(),
                    Item.getScrewHeight(),

                    Item.getClientOrganization()==null?ClientOrganizationDto.toDto():
                            ClientOrganizationDto.toDto(Item.getClientOrganization()),

                    Item.getSupplierOrganization()==null?SupplierDto.toDto():
                            SupplierDto.toDto(Item.getSupplierOrganization()),

//                    Item.getMakers()==null?MakerSimpleDto.toDtoList():
//                            MakerSimpleDto.toDtoList(Item.getMakers()),
                    Item.getMakers()==null?MakerSimpleDto.toDto():
                            MakerSimpleDto.toDto(Item.getMakers()),

                    Item.getPartNumber(),
                    //newItemMakerRepository.findByMaker(Item.getMakers().get(0).getMaker()).get(0).getPartnumber(),
                    Item.isRevise_progress(),

                    Item.getAttachments().
                            stream().
                            map( i->
                                    NewItemAttachmentDto.toDto
                                            (i,attachmentTagRepository)
                            )
                            .collect(toList()),

                    (char) Item.getRevision(),
                    "LIFECYCLE_NONE",
                    0L,
                    Item.getCreatedAt(),
                    MemberDto.toDto(Item.getMember(), defaultImageAddress),

                    Item.getModifier()==null?null:Item.getModifiedAt(),
                    Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier(), defaultImageAddress),

                    Item.isTempsave(),
                    Item.isReadonly(),
                    false,

                    -1L, //라우트 없으면 봄 안만들어짐

                    new BomDesignItemDto(
                            -1L,
                            tmpResponsibleDtoList
                    )

            );
        }
        return new NewItemDetailDto(
                Item.getId(),

                Item.getClassification()==null?
                        nullClassification
                        :ClassificationDto.toDto(Item.getClassification()),

                Item.getName(),

                Item.getItemTypes() == null?
                        nullItemTypesDto:
                        ItemTypesDto.toDto(Item.getItemTypes()),

                Item.getItemNumber(),
                Item.getThumbnail()==null?
                        nullImage:
                        NewItemImageDto.toDto(Item.getThumbnail()),
                Item.isSharing(),

                Item.getCarType()==null?
                        nullCarTypeDto:
                        CarTypeDto.toDto(Item.getCarType()),

                Item.getIntegrate(),
                Item.getCurve(),
                Item.getWidth(),
                Item.getHeight(),
                Item.getThickness(),
                Item.getWeight(),
                Item.getImportance(),

                Item.getColor()==null?
                        ColorDto.toDto():
                        ColorDto.toDto(Item.getColor()),
                Item.getLoadQuantity(),
                Item.getForming(),

                Item.getCoatingWay()==null?
                        CoatingDto.toDto():
                        CoatingDto.toDto(Item.getCoatingWay()),
                Item.getCoatingType()==null?
                        CoatingDto.toDto():
                        CoatingDto.toDto(Item.getCoatingType()),
                Item.getModulus(),
                Item.getScrew(),
                Item.getCuttingType(),
                Item.getLcd(),
                Item.getDisplaySize(),
                Item.getScrewHeight(),

                Item.getClientOrganization()==null?ClientOrganizationDto.toDto():
                        ClientOrganizationDto.toDto(Item.getClientOrganization()),

                Item.getSupplierOrganization()==null?SupplierDto.toDto():
                        SupplierDto.toDto(Item.getSupplierOrganization()),

//                Item.getMakers()==null?MakerSimpleDto.toDtoList():
//                        MakerSimpleDto.toDtoList(Item.getMakers()),

                Item.getMakers()==null?MakerSimpleDto.toDto():
                        MakerSimpleDto.toDto(Item.getMakers()),

                "no partnum",
                Item.isRevise_progress(),

                Item.getAttachments().
                        stream().
                        map( i->
                                NewItemAttachmentDto.toDto
                                        (i,attachmentTagRepository)
                        )
                        .collect(toList()),

                (char) Item.getRevision(),
                "LIFECYCLE_NONE",
                0L,
                Item.getCreatedAt(),
                MemberDto.toDto(Item.getMember(), defaultImageAddress),

                Item.getModifier()==null?null:Item.getModifiedAt(),
                Item.getModifier()==null?null:MemberDto.toDto(Item.getModifier(), defaultImageAddress),

                Item.isTempsave(),
                Item.isReadonly(),
                false, //라우트 없으면 거절될 일 없음

                -1L, //라우트 없으면 봄 안만들어짐

                new BomDesignItemDto(
                        -1L,
                        tmpResponsibleDtoList
                )
        );
    }

    private static boolean ItemPreRejected(RouteOrdering routeOrdering,
                                           RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);

        RouteProduct currentRouteProduct =
                routeProductList.get(routeOrdering.getPresent());


        if(Objects.equals(currentRouteProduct.getType().getModule(), "ITEM") &&
                Objects.equals(currentRouteProduct.getType().getName(), "CREATE")){
            if(currentRouteProduct.isPreRejected()){
                preRejected = true;
            }
        }

        return preRejected;
    }

}