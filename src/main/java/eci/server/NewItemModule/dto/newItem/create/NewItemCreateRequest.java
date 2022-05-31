package eci.server.NewItemModule.dto.newItem.create;

import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.exception.item.*;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.manufacture.MakerRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.NewItemImage;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.exception.ClassificationNotFoundException;
import eci.server.NewItemModule.exception.ClassificationRequiredException;
import eci.server.NewItemModule.exception.CoatingNotFoundException;
import eci.server.NewItemModule.exception.ItemTypeRequiredException;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import eci.server.NewItemModule.repository.supplier.SupplierRepository;
import eci.server.ProjectModule.exception.CarTypeNotFoundException;
import eci.server.ProjectModule.exception.ClientOrganizationNotFoundException;
import eci.server.ProjectModule.exception.ProduceOrganizationNotFoundException;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemCreateRequest {

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification1Id;

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification2Id;

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification3Id;

    @NotNull(message = "아이템 타입을 입력해주세요.")
    private Long typeId;
    //ItemTypes 으로 변환하기

    @Null
    private String itemNumber;

    @NotNull(message = "아이템 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "아이템 타입을 입력해주세요.")
    private boolean share;

    private Long carTypeId;

    // 맞춤 속성 ) boolean 빼고 다 만듦
    // integrate; curve;  forming;

    private String forming; //ChoiceField 5,6

    private String curve; //ChoiceField 3(평면),4 (곡면)

    private String integrate;

    private String width;

    private String height;

    private String thickness;

    private String weight;

    private String importance;

    private Long colorId;

    private Integer loadQuantity;

    private Long coatingWayId;

    private Long coatingTypeId;

    private Integer modulus;

    private String screw;

    private String cuttingType;

    private String lcd;

    private Integer displaySize;

    private Integer screwHeight;

    private Long clientOrganizationId;

    private Long supplierOrganizationId;

    private Long makersId;

    private List<MultipartFile> thumbnail = new ArrayList<>();

    private List<MultipartFile> attachments = new ArrayList<>();
    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();


    private List<Long> maker = new ArrayList<>();

    private List<String> partnumbers = new ArrayList<>();

    @Null
    private Long memberId;

    public static NewItem toEntity(
            NewItemCreateRequest req,
            Classification1Repository classification1Repository,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository,
            ItemTypesRepository itemTypesRepository,
            CarTypeRepository carTypeRepository,
            CoatingWayRepository coatingWayRepository,
            CoatingTypeRepository coatingTypeRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            SupplierRepository supplierRepository,
            MemberRepository memberRepository,
            ColorRepository colorRepository,
            MakerRepository makerRepository) {

        //분류 체크
        if(req.classification1Id==null || req.classification2Id ==null || req.classification3Id==null){
            throw new ClassificationRequiredException();
        }
        //아이템 타입 체크
        if(req.typeId==null){
            throw new ItemTypeRequiredException();
        }


        if (req.getTag().size() == 0) {
            // attachment 가 없을 경우
            return new NewItem(

                    new Classification(
                            classification1Repository.findById(req.classification1Id).orElseThrow(ClassificationNotFoundException::new),
                            classification2Repository.findById(req.classification2Id).orElseThrow(ClassificationNotFoundException::new),
                            classification3Repository.findById(req.classification3Id).orElseThrow(ClassificationNotFoundException::new)
                    ),

                    req.name,

                    itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new),

                    req.classification1Id + String.valueOf(ItemType.valueOf(
                            itemTypesRepository.findById(req.typeId).get().getItemType().name()
                    ).label() * 1000000 + (int) (Math.random() * 1000)),

                    req.thumbnail.stream().map(
                            i -> new NewItemImage(
                                    i.getOriginalFilename()
                            )
                    ).collect(
                            toList()
                    ),

                    req.share,

                    carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new),

                    req.integrate,

                    req.curve,

                    req.width,

                    req.height,

                    req.thickness,

                    req.weight,

                    req.importance,

                    colorRepository.findById(req.colorId).orElseThrow(ColorNotFoundException::new),

                    req.loadQuantity,

                    req.forming,

                    coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                    coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                    req.modulus,

                    req.screw,

                    req.cuttingType,

                    req.lcd,

                    req.displaySize,

                    req.screwHeight,

                    req.getClientOrganizationId() == null ? null :
                            clientOrganizationRepository.findById(req.getClientOrganizationId())
                                    .orElseThrow(ClientOrganizationNotFoundException::new),

                    req.getSupplierOrganizationId() == null ? null :
                            supplierRepository.findById(req.getSupplierOrganizationId())
                                    .orElseThrow(ProduceOrganizationNotFoundException::new),

                    req.maker.stream().map(
                            i ->
                                    makerRepository.
                                            findById(i).orElseThrow(ManufactureNotFoundException::new)
                    ).collect(
                            toList()
                    ),

                    req.partnumbers,


                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true, //임시저장 (라우트 작성 해야 false로 변한다)

                    false //revise progress 중 아니다

            );



        }


        // attachment 가 존재할 경우
        return new NewItem(

                new Classification(
                        classification1Repository.findById(req.classification1Id).orElseThrow(ClassificationNotFoundException::new),
                        classification2Repository.findById(req.classification2Id).orElseThrow(ClassificationNotFoundException::new),
                        classification3Repository.findById(req.classification3Id).orElseThrow(ClassificationNotFoundException::new)
                ),

                req.name,

                itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new),

                req.classification1Id + String.valueOf(ItemType.valueOf(
                        itemTypesRepository.findById(req.typeId).get().getItemType().name()
                ).label() * 1000000 + (int) (Math.random() * 1000)),

                req.thumbnail.stream().map(
                        i -> new NewItemImage(
                                i.getOriginalFilename()
                        )
                ).collect(
                        toList()
                ),

                req.share,

                carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new),

                req.integrate,

                req.curve,

                req.width,

                req.height,

                req.thickness,

                req.weight,

                req.importance,

                colorRepository.findById(req.colorId).orElseThrow(ColorNotFoundException::new),

                req.loadQuantity,

                req.forming,

                coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                req.modulus,

                req.screw,

                req.cuttingType,

                req.lcd,

                req.displaySize,

                req.screwHeight,

                req.getClientOrganizationId() == null ? null :
                        clientOrganizationRepository.findById(req.getClientOrganizationId())
                                .orElseThrow(ClientOrganizationNotFoundException::new),

                req.getSupplierOrganizationId() == null ? null :
                        supplierRepository.findById(req.getSupplierOrganizationId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new),

                req.maker.stream().map(
                        i ->
                                makerRepository.
                                        findById(i).orElseThrow(ManufactureNotFoundException::new)
                ).collect(
                        toList()
                ),

                req.partnumbers,


                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                true, //임시저장 (라우트 작성 해야 false로 변한다)

                false, //revise progress 중 아니다


                req.attachments.stream().map(
                        i -> new NewItemAttachment(
                                i.getOriginalFilename(),
                                req.getTag().get(req.attachments.indexOf(i)),
                                req.getAttachmentComment().get(req.attachments.indexOf(i))
                        )
                ).collect(
                        toList()
                )


        );
    }

}