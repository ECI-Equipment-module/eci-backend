package eci.server.NewItemModule.dto.newItem.create;

import eci.server.DocumentModule.entity.DocumentAttachment;
import eci.server.DocumentModule.exception.DocumentNotFoundException;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.exception.item.*;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemTypesRepository;

import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.NewItemImage;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.exception.*;
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
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
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

    @NotNull(message = "?????? ????????? ??????????????????.")
    private Long classification1Id;

    @NotNull(message = "?????? ????????? ??????????????????.")
    private Long classification2Id;

    @NotNull(message = "?????? ????????? ??????????????????.")
    private Long classification3Id;

    @NotNull(message = "????????? ????????? ??????????????????.")
    private Long typeId;
    //ItemTypes ?????? ????????????

    @Null
    private String itemNumber;

    @NotNull(message = "????????? ????????? ??????????????????.")
    private String name;

    @NotNull(message = "????????? ????????? ??????????????????.")
    private boolean sharing;

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
    //private List<String> tag = new ArrayList<>();
    private List<Long> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();


    //private List<Long> makersId = new ArrayList<>();
    private Long makersId;

    //private List<String> partnumbers = new ArrayList<>();
    private String partnumbers;

    @Null
    private Long memberId;

    @Nullable
    private List<Long> duplicateTargetIds;

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
            MakerRepository makerRepository,
            AttachmentTagRepository attachmentTagRepository,
            NewItemAttachmentRepository newItemAttachmentRepository) {

        //?????? ??????
        if(req.classification1Id==null || req.classification2Id ==null || req.classification3Id==null){
            throw new ClassificationRequiredException();
        }

        if(req.getClassification1Id()==99999L || req.getClassification2Id() == 99999L){
            //06-18 ?????? 3??? 99999 ?????? ?????????
            throw new ProperClassificationRequiredException();
        }

        if(req.classification1Id==99999L && req.classification2Id ==99999L && req.classification3Id==99999L){
            throw new ProperClassificationRequiredException();
        }

        //????????? ?????? ??????
        if(req.typeId==null){
            throw new ItemTypeRequiredException();
        }

        //????????? ?????? ??????
        if(req.name.isBlank()){
            throw new ItemNameRequiredException();
        }


        /**
         * ?????? ??? ??? ??? ?????? ??????,????????? ?????? ??????
         */
        // 0) ?????? ????????? ?????? / ?????? ????????? ????????? ????????? ??? ??? ?????? ??????/ ??? ?????? ????????? ????????? ?????????
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment =  new ArrayList<>();

        if(req.getDuplicateTargetIds()!=null && req.getDuplicateTargetIds().size()>0){

            int standardIdx = req.getDuplicateTargetIds().size();

            // tag ??????
            oldDocTag = new ArrayList<>(req.getTag().subList(0,standardIdx));
            newDocTag = new ArrayList<>(req.getTag().subList(standardIdx, req.getTag().size()));
            // comment ??????
            oldDocComment = new ArrayList<>(req.getAttachmentComment().subList(0,standardIdx));
            newDocComment = new ArrayList<>(req.getAttachmentComment().subList(standardIdx, req.getTag().size()));

        }else{//(req.getAttachments()!=null && req.getAttachments().size()>0){
            newDocTag.addAll(req.getTag());
            newDocComment.addAll(req.getAttachmentComment());
        }

        /**
         * ?????? ???????????? ?????? ????????? ????????? ?????????
         * ??? ?????? ????????? ????????? new DoucumentAttachment???
         * ???????????? ???????????? ?????????
         * ????????? null ???????????? ???
         */

        List<NewItemAttachment> duplicateNewDocumentAttachments = new ArrayList<>();

        if(req.getDuplicateTargetIds()!=null){
            // 1) ????????? ?????? ?????? ?????????
            List<NewItemAttachment> duplicatedTargetAttaches =
                    req.getDuplicateTargetIds().stream().map(
                            o-> newItemAttachmentRepository.findById(
                                    o
                            ).orElseThrow(DocumentNotFoundException::new)
                    ).collect(toList());


            int idx = 0;
            // 2) ????????? ????????? ?????? Document Attachment ??? ???????????????
            for(NewItemAttachment d : duplicatedTargetAttaches){

                duplicateNewDocumentAttachments.add(new NewItemAttachment(
                                    d.getOriginName(),
                                    d.getUniqueName(),
                                    d.getAttachmentaddress(),
                                    "????????? ?????? ?????????",
                                    true,

                        attachmentTagRepository
                                .findById(oldDocTag.get(idx)).
                                orElseThrow(AttachmentTagNotFoundException::new).getName(),

                        oldDocComment.size()==0?
                                " "
                                :oldDocComment.get(idx).isBlank()?" " :
                                oldDocComment.get(idx)
                        )


                );

                idx+=1;
            }

        }



        if(req.getAttachments()==null || req.getAttachments().size()==0) {

            // attachment ??? ?????? ?????? & ???????????? ??????

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

                    req.getThumbnail()==null?
                            null
                            :
                            new NewItemImage(
                                    req.thumbnail.getOriginalFilename()
                            ),

                    req.sharing,

                    //????????? ?????? ?????? ??????
                    (!req.isSharing())?
                            //1. ???????????????
                            req.getCarTypeId()==null?
                                    //1-1 : ????????? ????????? (????????? ?????? ????????????
                                    carTypeRepository.findById(-1L).orElseThrow(CarTypeNotFoundException::new):
                                    //null ????????? ???????????? ???
                                    carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new)

                            :
                            //2. ???????????????
                            null,


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

                    req.forming.isBlank()?"":req.forming,

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

                    req.lcd.isBlank()?"":req.lcd,

                    req.displaySize.isBlank()?null:req.displaySize,

                    req.screwHeight.isBlank()?null:req.screwHeight,

                    req.getClientOrganizationId() == null ?
                            null
//                            clientOrganizationRepository.findById(99999L)
//                                    .orElseThrow(ClientOrganizationNotFoundException::new)
            :
                            clientOrganizationRepository.findById(req.getClientOrganizationId())
                                    .orElseThrow(ClientOrganizationNotFoundException::new),

                    req.getSupplierOrganizationId() == null ?
                            null:
//                            supplierRepository.findById(99999L)
//                                    .orElseThrow(ProduceOrganizationNotFoundException::new):
                            supplierRepository.findById(req.getSupplierOrganizationId())
                                    .orElseThrow(ProduceOrganizationNotFoundException::new),

//                    req.makersId.stream().map(
//                            i ->
//                                    makerRepository.
//                                            findById(i).orElseThrow(ManufactureNotFoundException::new)
//                    ).collect(
////                            toList()
////                    ),

                    req.getMakersId()==null?
                            null:
                            //makerRepository.findById(req.getMakersId()).orElseThrow(MakerNotFoundException::new):
                            makerRepository.findById(req.getMakersId()).orElseThrow(MakerNotFoundException::new),

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

        // attachment dto ?????? ??????????????????
        List<NewItemAttachment> newItemAttachments = new ArrayList<>();
        for(MultipartFile i : req.attachments){
            int d =0;
            newItemAttachments.add(
                    new NewItemAttachment(
                            i.getOriginalFilename(),
                            attachmentTagRepository
                                    .findById(newDocTag.get(d)).
                                    orElseThrow(AttachmentTagNotFoundException::new)
                                    .getName(),

                            newDocComment.size()==0? " ":
                            newDocComment.get(d).isBlank()?" ":newDocComment.get(d)
                            ,
                            //??? ??????????????? ?????? ???????????? ????????? ?????? save = true
                            true //save ?????????
                    )
            );
                    d+=1;
        }


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

                req.getThumbnail()==null?
                        null
                        :
                        new NewItemImage(
                                req.thumbnail.getOriginalFilename()
                ),

                req.sharing,

                //????????? ?????? ?????? ??????
                (!req.isSharing())?
                        //1. ???????????????
                        req.getCarTypeId()==null?
                                //1-1 : ????????? ????????? (????????? ?????? ????????????
                                carTypeRepository.findById(-1L).orElseThrow(CarTypeNotFoundException::new):
                                //null ????????? ???????????? ???
                                carTypeRepository.findById(req.carTypeId).orElseThrow(CarTypeNotFoundException::new)

                        :
                        //2. ???????????????
                        null,

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

                req.forming.isBlank()?"":req.forming,

                req.getCoatingWayId()==null?
                        null:
                        //coatingWayRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                        coatingWayRepository.findById(req.coatingWayId).orElseThrow(CoatingNotFoundException::new),

                req.getCoatingTypeId()==null?
                        null:
                        //coatingTypeRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new):
                        coatingTypeRepository.findById(req.coatingTypeId).orElseThrow(CoatingNotFoundException::new),

                req.modulus.toString().isBlank()?"":req.modulus,

                req.screw.isBlank()?"":req.screw,

                req.cuttingType.isBlank()?"":req.cuttingType,

                req.lcd.isBlank()?"":req.cuttingType,

                req.displaySize.isBlank()?null:req.displaySize,

                req.screwHeight.isBlank()?null:req.screwHeight,

                req.getClientOrganizationId() == null ?
                        null
//                        clientOrganizationRepository.findById(99999L)
//                                .orElseThrow(ClientOrganizationNotFoundException::new)
                        :
                        clientOrganizationRepository.findById(req.getClientOrganizationId())
                                .orElseThrow(ClientOrganizationNotFoundException::new),

                req.getSupplierOrganizationId() == null ?
//                        supplierRepository.findById(99999L)
//                                .orElseThrow(ProduceOrganizationNotFoundException::new):
                        null:
                        supplierRepository.findById(req.getSupplierOrganizationId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new),

//                req.makersId.stream().map(
//                        i ->
//                                makerRepository.
//                                        findById(i).orElseThrow(ManufactureNotFoundException::new)
//                ).collect(
//                        toList()
//                ),

                req.getMakersId()==null?
                        null:
                        //makerRepository.findById(-1L).orElseThrow(MakerNotFoundException::new):
                        makerRepository.findById(req.getMakersId()).orElseThrow(MakerNotFoundException::new)
,

                req.partnumbers,


                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                true, //???????????? (????????? ?????? ?????? false??? ?????????)

                false ,//revise progress ??? ?????????

                newItemAttachments,

                duplicateNewDocumentAttachments

        );

    }



}
