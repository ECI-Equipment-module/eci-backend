package eci.server.NewItemModule.dto.newItem.create;

import com.sun.istack.Nullable;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ColorNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.NewItemImage;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.exception.ClassificationNotFoundException;
import eci.server.NewItemModule.exception.CoatingNotFoundException;
import eci.server.NewItemModule.exception.MakerNotFoundException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import eci.server.NewItemModule.repository.maker.MakerRepository;
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

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemTemporaryCreateRequest {

    private Long classification1Id;

    private Long classification2Id;

    private Long classification3Id;

    private Long typeId;
    //ItemTypes 으로 변환하기

    @Null
    private String itemNumber;
    private String name;

    private Boolean sharing;

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

    private String loadQuantity;

    private Long coatingWayId;

    private Long coatingTypeId;

    private String modulus;

    private String screw;

    private String cuttingType;

    private String lcd;

    private String displaySize;

    private String screwHeight;

    private Long clientOrganizationId;

    private Long supplierOrganizationId;

    @Nullable
    private MultipartFile thumbnail;

    private List<MultipartFile> attachments = new ArrayList<>();
    private List<Long> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();


    private Long makersId;

    //private List<String> partnumbers = new ArrayList<>();
    private String partnumbers;
    @Null
    private Long memberId;

    public static NewItem toEntity(
            NewItemTemporaryCreateRequest req,
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
            MakerRepository makerRepository,
            AttachmentTagRepository attachmentTagRepository) {

        if (req.getTag().size() == 0) {


            // attachment 가 없을 경우
            return new NewItem(

                        new Classification(

                                (req.getClassification1Id()==null  || req.getClassification1Id()==99999L?
                                classification1Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                classification1Repository.findById(req.classification1Id).orElseThrow(ClassificationNotFoundException::new)
                                ),

                                (req.getClassification2Id()==null  || req.getClassification2Id()==99999L?
                                        classification2Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new):
                                        classification2Repository.findById(req.classification2Id).orElseThrow(ClassificationNotFoundException::new)
                                ),
                    (req.getClassification3Id()==null  || req.getClassification3Id()==99999L?
                    classification3Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new):
                                        classification3Repository.findById(req.classification3Id).orElseThrow(ClassificationNotFoundException::new)
                        )
            )
            ,

                    req.name.isBlank() ? "이름을 입력해주세요" : req.name,

                    req.getTypeId()==null?
                            itemTypesRepository.findById(99999L).orElseThrow(ItemNotFoundException::new):
                            itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new),

                    "made when saved",

                    //TODO : THUMBNAIL 없어도 되기 0614
                    (
                            (req.getThumbnail().getSize()>0)?
                                    new NewItemImage(
                                            req.thumbnail.getOriginalFilename()
                                    )
                                    :null
                    ),

                    req.getSharing() == null || req.getSharing().toString().isBlank() || req.sharing,

                    //전용일 때야 차종 생성
                    req.getCarTypeId()!=null?
                            carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new)
                            :carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new),


                    req.integrate.isBlank()?"":req.integrate,

                    req.curve.isBlank()?"":req.curve,

                    req.width.isBlank()?"":req.width,

                    req.height.isBlank()?"":req.height,

                    req.thickness.isBlank()?"":req.thickness,

                    req.weight.isBlank()?"":req.weight,

                    req.importance.isBlank()?"":req.importance,

                    req.getColorId()==null?
                            null:
                            //colorRepository.findById(99999L).orElseThrow(ColorNotFoundException::new):
                            colorRepository.findById(req.colorId).orElseThrow(ColorNotFoundException::new),

                    req.loadQuantity.isBlank()?"":req.loadQuantity,

                    req.getForming()==null||req.forming.isBlank()?"":req.forming,

                    req.getCoatingWayId()==null?
                            null:
                            //coatingWayRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                            coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                    req.getCoatingTypeId()==null?
                            null:
                            //coatingTypeRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                            coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                    req.modulus.isBlank()?"":req.modulus,

                    req.screw.isBlank()?"":req.screw,

                    req.cuttingType.isBlank()?"":req.cuttingType,

                    req.lcd.isBlank()?"":req.cuttingType,

                    req.displaySize.isBlank()?null:req.displaySize,

                    req.screwHeight.isBlank()?null:req.screwHeight,

                    req.getClientOrganizationId() == null ?
                            null
                            //clientOrganizationRepository.findById(99999L)
                            //        .orElseThrow(ClientOrganizationNotFoundException::new)
                            :
                            clientOrganizationRepository.findById(req.getClientOrganizationId())
                                    .orElseThrow(ClientOrganizationNotFoundException::new),

                    req.getSupplierOrganizationId() == null ?
                            null
                            :
//                            supplierRepository.findById(99999L)
//                                    .orElseThrow(ProduceOrganizationNotFoundException::new):
                            supplierRepository.findById(req.getSupplierOrganizationId())
                                    .orElseThrow(ProduceOrganizationNotFoundException::new),

                    req.getMakersId()==null?
                            null
                            //makerRepository.findById(99999L).orElseThrow(MemberNotFoundException::new)
                            :makerRepository.findById(req.getMakersId()).orElseThrow(MemberNotFoundException::new),
//                    req.makersId.stream().map(
//                            i ->
//                                    makerRepository.
//                                            findById(i).orElseThrow(ManufactureNotFoundException::new)
//                    ).collect(
//                            toList()
//                    ),

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

                        (req.getClassification1Id()==null  || req.getClassification1Id()==99999L?
                                classification1Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                classification1Repository.findById(req.classification1Id).orElseThrow(ClassificationNotFoundException::new)
                        ),

                        (req.getClassification2Id()==null  || req.getClassification2Id()==99999L?
                                classification2Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new):
                                classification2Repository.findById(req.classification2Id).orElseThrow(ClassificationNotFoundException::new)
                        ),
                        (req.getClassification3Id()==null  || req.getClassification3Id()==99999L?
                                classification3Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new):
                                classification3Repository.findById(req.classification3Id).orElseThrow(ClassificationNotFoundException::new)
                        )
                )
                ,

                req.name.isBlank() ? "이름을 입력해주세요" : req.name,

                req.getTypeId()==null?
                        itemTypesRepository.findById(99999L).orElseThrow(ItemNotFoundException::new):
                        itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new),

                "made when saved",

                //TODO : THUMBNAIL 없어도 되기 0614
                (
                        (req.getThumbnail().getSize()>0)?
                                new NewItemImage(
                                        req.thumbnail.getOriginalFilename()
                                )
                                :null
                ),

                req.getSharing() == null || req.getSharing().toString().isBlank() || req.sharing,

                //전용일 때야 차종 생성
                req.getCarTypeId()!=null?
                        carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new)
                        :carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new),


                req.integrate.isBlank()?"":req.integrate,

                req.curve.isBlank()?"":req.curve,

                req.width.isBlank()?"":req.width,

                req.height.isBlank()?"":req.height,

                req.thickness.isBlank()?"":req.thickness,

                req.weight.isBlank()?"":req.weight,

                req.importance.isBlank()?"":req.importance,

                req.getColorId()==null?
                        null:
                        //colorRepository.findById(99999L).orElseThrow(ColorNotFoundException::new):
                        colorRepository.findById(req.colorId).orElseThrow(ColorNotFoundException::new),

                req.loadQuantity.isBlank()?"":req.loadQuantity,

                req.getForming()==null||req.forming.isBlank()?"":req.forming,

                req.getCoatingWayId()==null?
                        null:
                        //coatingWayRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                        coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                req.getCoatingTypeId()==null?
                        null:
                        //coatingTypeRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                        coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                req.modulus.isBlank()?"":req.modulus,

                req.screw.isBlank()?"":req.screw,

                req.cuttingType.isBlank()?"":req.cuttingType,

                req.lcd.isBlank()?"":req.cuttingType,

                req.displaySize.isBlank()?null:req.displaySize,

                req.screwHeight.isBlank()?null:req.screwHeight,

                req.getClientOrganizationId() == null ?
                        null
                        //clientOrganizationRepository.findById(99999L)
                        //        .orElseThrow(ClientOrganizationNotFoundException::new)
                        :
                        clientOrganizationRepository.findById(req.getClientOrganizationId())
                                .orElseThrow(ClientOrganizationNotFoundException::new),

                req.getSupplierOrganizationId() == null ?
                        null
                        :
//                            supplierRepository.findById(99999L)
//                                    .orElseThrow(ProduceOrganizationNotFoundException::new):
                        supplierRepository.findById(req.getSupplierOrganizationId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new),

                req.getMakersId()==null?
                        null
                        //makerRepository.findById(99999L).orElseThrow(MemberNotFoundException::new)
                        :makerRepository.findById(req.getMakersId()).orElseThrow(MemberNotFoundException::new),
//                    req.makersId.stream().map(
//                            i ->
//                                    makerRepository.
//                                            findById(i).orElseThrow(ManufactureNotFoundException::new)
//                    ).collect(
//                            toList()
//                    ),

                req.partnumbers,


                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                true, //임시저장 (라우트 작성 해야 false로 변한다)

                false, //revise progress 중 아니다


                req.attachments.stream().map(
                        i -> new NewItemAttachment(
                                i.getOriginalFilename(),
                                attachmentTagRepository
                                        .findById(req.getTag().get(req.attachments.indexOf(i))).
                                        orElseThrow(AttachmentNotFoundException::new).getName(),
                                req.getAttachmentComment().get(req.attachments.indexOf(i)),
                                false //지금은 임시저장으로 추가되는 문서들 => save = false 다
                        )
                ).collect(
                        toList()
                )
        );
    }

}