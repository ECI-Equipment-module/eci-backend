package eci.server.NewItemModule.dto.newItem.create;

import com.sun.istack.Nullable;
import eci.server.DocumentModule.entity.DocumentAttachment;
import eci.server.DocumentModule.exception.DocumentNotFoundException;
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
import eci.server.NewItemModule.repository.attachment.NewItemAttachmentRepository;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Lists;

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
    //ItemTypes ?????? ????????????

    @Null
    private String itemNumber;
    private String name;

    private Boolean sharing;

    private Long carTypeId;

    // ?????? ?????? ) boolean ?????? ??? ??????
    // integrate; curve;  forming;

    private String forming; //ChoiceField 5,6

    private String curve; //ChoiceField 3(??????),4 (??????)

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

    @org.springframework.lang.Nullable
    private List<Long> duplicateTargetIds = new ArrayList<>();

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
            AttachmentTagRepository attachmentTagRepository,
            NewItemAttachmentRepository newItemAttachmentRepository) {


        /**
         * ?????? ???????????? ?????? ????????? ????????? ?????????
         * ??? ?????? ????????? ????????? new DoucumentAttachment???
         * ???????????? ???????????? ?????????
         * ????????? null ???????????? ???
         */

        List<NewItemAttachment> duplicateNewDocumentAttachments = null;

        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        if (req.getDuplicateTargetIds() != null) {
            // 1) ????????? ?????? ?????? ?????????
            List<NewItemAttachment> duplicatedTargetAttaches =
                    req.getDuplicateTargetIds().stream().map(
                            o -> newItemAttachmentRepository.findById(
                                    o
                            ).orElseThrow(DocumentNotFoundException::new)
                    ).collect(toList());

            if (req.getDuplicateTargetIds() != null && req.getDuplicateTargetIds().size() > 0) {

                int standardIdx = req.getDuplicateTargetIds().size();

                // tag ??????
                oldDocTag.addAll(req.getTag().subList(0, standardIdx));
                newDocTag.addAll(req.getTag().subList(standardIdx, req.getTag().size()));
                // comment ??????
                oldDocComment.addAll(req.getAttachmentComment().subList(0, standardIdx));
                newDocComment.addAll(req.getAttachmentComment().subList(standardIdx, req.getTag().size()));

            } else {//(req.getAttachments()!=null && req.getAttachments().size()>0){
                newDocTag.addAll(req.getTag());
                newDocComment.addAll(req.getAttachmentComment());
            }

            // 2) ????????? ????????? new Document Attachment ??? ???????????????
            duplicateNewDocumentAttachments =
                    duplicatedTargetAttaches.stream().map(
                            d -> new NewItemAttachment(
                                    d.getOriginName(),
                                    d.getUniqueName(),
                                    d.getAttachmentaddress(),
                                    "????????? ?????? ?????????",

                                    false, //????????? ?????????????????? ???????????? ????????? => save = false ???

                                    attachmentTagRepository.findById(
                                            oldDocTag.get(duplicatedTargetAttaches.indexOf(d))
                                    ).orElseThrow(AttachmentNotFoundException::new).getName(),

                                    oldDocComment.size()==0||
                                    oldDocComment.get(duplicatedTargetAttaches.indexOf(d)) == null
                                            ||
                                            oldDocComment.get(duplicatedTargetAttaches.indexOf(d)).isBlank()
                                            ?
                                            " " :
                                            oldDocComment.get(duplicatedTargetAttaches.indexOf(d))

                            )
                    ).collect(toList());
        }

        if (req.getAttachments() == null || req.getAttachments().size() == 0) {


            // attachment ??? ?????? ??????
            return new NewItem(

                    new Classification(

                            (req.getClassification1Id() == null || req.getClassification1Id() == 99999L ?
                                    classification1Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                    classification1Repository.findById(req.classification1Id).orElseThrow(ClassificationNotFoundException::new)
                            ),

                            (req.getClassification2Id() == null || req.getClassification2Id() == 99999L ?
                                    classification2Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                    classification2Repository.findById(req.classification2Id).orElseThrow(ClassificationNotFoundException::new)
                            ),
                            (req.getClassification3Id() == null || req.getClassification3Id() == 99999L ?
                                    classification3Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                    classification3Repository.findById(req.classification3Id).orElseThrow(ClassificationNotFoundException::new)
                            )
                    )
                    ,

                    req.name.isBlank() ? "????????? ??????????????????" : req.name,

                    req.getTypeId() == null ?
                            null :
                            itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new),

                    "made when saved",

                    req.getThumbnail() == null ?
                            null
                            :
                            new NewItemImage(
                                    req.thumbnail.getOriginalFilename()
                            ),

                    req.getSharing() == null || req.getSharing().toString().isBlank() || req.sharing,

                    //????????? ?????? ?????? ??????
                    req.getCarTypeId() != null ?
                            carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new)
                            : null,


                    req.integrate.isBlank() ? "" : req.integrate,

                    req.curve.isBlank() ? "" : req.curve,

                    req.width.isBlank() ? "" : req.width,

                    req.height.isBlank() ? "" : req.height,

                    req.thickness.isBlank() ? "" : req.thickness,

                    req.weight.isBlank() ? "" : req.weight,

                    req.importance.isBlank() ? "" : req.importance,

                    req.getColorId() == null ?
                            null :
                            //colorRepository.findById(99999L).orElseThrow(ColorNotFoundException::new):
                            colorRepository.findById(req.colorId).orElseThrow(ColorNotFoundException::new),

                    req.loadQuantity.isBlank() ? "" : req.loadQuantity,

                    req.getForming() == null || req.forming.isBlank() ? "" : req.forming,

                    req.getCoatingWayId() == null ?
                            null :
                            //coatingWayRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                            coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                    req.getCoatingTypeId() == null ?
                            null :
                            //coatingTypeRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                            coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                    req.modulus.isBlank() ? "" : req.modulus,

                    req.screw.isBlank() ? "" : req.screw,

                    req.cuttingType.isBlank() ? "" : req.cuttingType,

                    req.lcd.isBlank() ? "" : req.cuttingType,

                    req.displaySize.isBlank() ? null : req.displaySize,

                    req.screwHeight.isBlank() ? null : req.screwHeight,

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

                    req.getMakersId() == null ?
                            null
                            //makerRepository.findById(99999L).orElseThrow(MemberNotFoundException::new)
                            : makerRepository.findById(req.getMakersId()).orElseThrow(MemberNotFoundException::new),
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

                    true, //???????????? (????????? ?????? ?????? false??? ?????????)

                    false, //revise progress ??? ?????????

                    duplicateNewDocumentAttachments
            );


        }


        // attachment ??? ????????? ??????

        return new NewItem(

                new Classification(

                        (req.getClassification1Id() == null || req.getClassification1Id() == 99999L ?
                                classification1Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                classification1Repository.findById(req.classification1Id).orElseThrow(ClassificationNotFoundException::new)
                        ),

                        (req.getClassification2Id() == null || req.getClassification2Id() == 99999L ?
                                classification2Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                classification2Repository.findById(req.classification2Id).orElseThrow(ClassificationNotFoundException::new)
                        ),
                        (req.getClassification3Id() == null || req.getClassification3Id() == 99999L ?
                                classification3Repository.findById(99999L).orElseThrow(ClassificationNotFoundException::new) :
                                classification3Repository.findById(req.classification3Id).orElseThrow(ClassificationNotFoundException::new)
                        )
                )
                ,

                req.name.isBlank() ? "????????? ??????????????????" : req.name,

                req.getTypeId() == null ?
                        null :
                        //itemTypesRepository.findById(99999L).orElseThrow(ItemNotFoundException::new):
                        itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new),

                "made when saved",

                req.getThumbnail() == null ?
                        null
                        :
                        new NewItemImage(
                                req.thumbnail.getOriginalFilename()
                        ),

                req.getSharing() == null || req.getSharing().toString().isBlank() || req.sharing,

                //????????? ?????? ?????? ??????
                req.getCarTypeId() != null ?
                        carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new)
                        : null,//carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new),


                req.integrate.isBlank() ? "" : req.integrate,

                req.curve.isBlank() ? "" : req.curve,

                req.width.isBlank() ? "" : req.width,

                req.height.isBlank() ? "" : req.height,

                req.thickness.isBlank() ? "" : req.thickness,

                req.weight.isBlank() ? "" : req.weight,

                req.importance.isBlank() ? "" : req.importance,

                req.getColorId() == null ?
                        null :
                        //colorRepository.findById(99999L).orElseThrow(ColorNotFoundException::new):
                        colorRepository.findById(req.colorId).orElseThrow(ColorNotFoundException::new),

                req.loadQuantity.isBlank() ? "" : req.loadQuantity,

                req.getForming() == null || req.forming.isBlank() ? "" : req.forming,

                req.getCoatingWayId() == null ?
                        null :
                        //coatingWayRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                        coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                req.getCoatingTypeId() == null ?
                        null :
                        //coatingTypeRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                        coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                req.modulus.isBlank() ? "" : req.modulus,

                req.screw.isBlank() ? "" : req.screw,

                req.cuttingType.isBlank() ? "" : req.cuttingType,

                req.lcd.isBlank() ? "" : req.cuttingType,

                req.displaySize.isBlank() ? null : req.displaySize,

                req.screwHeight.isBlank() ? null : req.screwHeight,

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

                req.getMakersId() == null ?
                        null
                        //makerRepository.findById(99999L).orElseThrow(MemberNotFoundException::new)
                        : makerRepository.findById(req.getMakersId()).orElseThrow(MemberNotFoundException::new),
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

                true, //???????????? (????????? ?????? ?????? false??? ?????????)

                false, //revise progress ??? ?????????


//                req.attachments.stream().map(
//                        i -> new NewItemAttachment(
//                                i.getOriginalFilename(),
//                                attachmentTagRepository
//                                        .findById(req.getTag().get(req.attachments.indexOf(i))).
//                                        orElseThrow(AttachmentNotFoundException::new).getName(),
//
//                                req.getAttachmentComment().isEmpty()?
//                                        "":req.getAttachmentComment().get(
//                                        req.attachments.indexOf(i)
//                                ),
//                                false //????????? ?????????????????? ???????????? ????????? => save = false ???
//                        )
//                ).collect(
//                        toList()
//                ),
                req.attachments.stream().map(
                        i -> new NewItemAttachment(
                                i.getOriginalFilename(),
                                attachmentTagRepository//////////////////////////////
                                        .findById(newDocTag.get(req.attachments.indexOf(i))).
                                        orElseThrow(AttachmentNotFoundException::new).getName(),

                                newDocComment.size()==0?" "
                                :newDocComment.get(
                                        req.attachments.indexOf(i)
                                        )
                                        .isBlank() ?
                                        " " : newDocComment.get(
                                        req.attachments.indexOf(i)
                                ),
                                false //????????? ?????????????????? ???????????? ????????? => save = false ???
                        )
                ).collect(
                        toList()
                ),
                duplicateNewDocumentAttachments
        );


    }
}


//
//    @Getter
//    @AllArgsConstructor
//    class ProduceNewOlgTagComment {
//        private List<Long> oldDocTag;
//        private List<Long> newDocTag;
//        private List<String> oldDocComment;
//        private List<String> newDocComment;
//    }
//
//
//    public static ProduceNewOlgTagComment toNewOldList(
//            NewItemTemporaryCreateRequest req,
//            NewItemAttachmentRepository newItemAttachmentRepository
//
//    ) {
//        List<Long> oldDocTag = new ArrayList<>();
//        List<Long> newDocTag = new ArrayList<>();
//        List<String> oldDocComment = new ArrayList<>();
//        List<String> newDocComment = new ArrayList<>();
//
//        if (req.getDuplicateTargetIds() != null) {
//            // 1) ????????? ?????? ?????? ?????????
//            List<NewItemAttachment> duplicatedTargetAttaches =
//                    req.getDuplicateTargetIds().stream().map(
//                            o -> newItemAttachmentRepository.findById(
//                                    o
//                            ).orElseThrow(DocumentNotFoundException::new)
//                    ).collect(toList());
//
//            if (req.getDuplicateTargetIds() != null && req.getDuplicateTargetIds().size() > 0) {
//
//                int standardIdx = req.getDuplicateTargetIds().size();
//
//                // tag ??????
//                oldDocTag.addAll(req.getTag().subList(0, standardIdx));
//                newDocTag.addAll(req.getTag().subList(standardIdx, req.getTag().size()));
//                // comment ??????
//                oldDocComment.addAll(req.getAttachmentComment().subList(0, standardIdx));
//                newDocComment.addAll(req.getAttachmentComment().subList(standardIdx, req.getTag().size()));
//
//            } else {//(req.getAttachments()!=null && req.getAttachments().size()>0){
//                newDocTag.addAll(req.getTag());
//                newDocComment.addAll(req.getAttachmentComment());
//            }
//
//
//
//
//        }
//        return new ProduceNewOlgTagComment(
//                oldDocTag,
//                newDocTag,
//                oldDocComment,
//                newDocComment
//        );
//
//    }
