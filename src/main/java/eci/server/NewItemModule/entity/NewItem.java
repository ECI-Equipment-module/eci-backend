package eci.server.NewItemModule.entity;

import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.exception.item.ColorNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.NewItemModule.entity.supplier.Maker;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.coating.CoatingType;
import eci.server.NewItemModule.entity.coating.CoatingWay;
import eci.server.NewItemModule.entity.supplier.Supplier;
import eci.server.NewItemModule.exception.CoatingNotFoundException;
import eci.server.NewItemModule.exception.SupplierNotFoundException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.exception.*;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import eci.server.NewItemModule.repository.maker.MakerRepository;
import eci.server.NewItemModule.repository.supplier.SupplierRepository;
import eci.server.ProjectModule.entity.project.CarType;
import eci.server.ProjectModule.entity.project.ClientOrganization;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.CarTypeNotFoundException;
import eci.server.ProjectModule.exception.ClientOrganizationNotFoundException;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewItem extends EntityDate {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE3")
    @SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c1_id", nullable = false)
    @JoinColumn(name = "c2_id", nullable = false)
    @JoinColumn(name = "c3_id", nullable = false)
    private Classification classification;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "itemTypes_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTypes itemTypes;

    @Column(nullable = false)
    private String itemNumber;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "newItem"
    ) //????????? ?????? ??????????????????
    // Item??? NewItemImage?????? fk ??? null??? ??????
    // (?????? ????????????) image ??????
    private NewItemImage thumbnail;

    /**
     * true??? ??????, false??? ??????
     */
    @Column
    private boolean sharing;

    /**
     * sharing ??? false ??? ??????!
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;

    /**
     * ??????/?????? true ???  ??????, false??? ??????
     */
    @Column
    private String integrate;

    /**
     * true??? ??????, false??? ??????
     */
    @Column
    private String curve;

    //??????
    @Column
    private String width;

    //??????
    @Column
    private String height;

    //??????
    @Column
    private String thickness;

    //??????
    @Column
    private String weight;

    //??????
    @Column
    private String importance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Color color;

    //?????? ??????
    @Column
    private String loadQuantity;

    //?????? ??????
    @Column
    private String forming;

    //?????? ??????
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coating_way_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoatingWay coatingWay;

    //?????? ??????
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coating_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoatingType coatingType;

    //???????????? ?????? ??????
    @Column
    private String modulus;

    //?????? (?????? , ??????)
    @Column
    private String screw;

    //???????????? : ?????????, ??????
    @Column
    private String cuttingType;

    //LCD : TDT , SEG, OLED
    @Column
    private String lcd;

    //Display ?????????
    @Column
    private String displaySize;

    //?????????
    @Column
    private String screwHeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Supplier supplierOrganization;

    //    @OneToMany(
//            mappedBy = "newItem",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "makers")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Maker makers;

    private String partNumber;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "supplier_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<NewItemMaker> makers;

    @Column(nullable = false)
    private boolean tempsave;

    @Column(nullable = false)
    private boolean readonly;

    @Column//0613 ?????? - CAD?????? ?????? ??????, ???????????? false
    private boolean subAssy;

    @Column(nullable = false)
    private boolean revise_progress;

    @Column
    private int revision;

    @Column
    private Integer released;

    @Column//nullable ?????? - ?????? ???????????? revise copying new item ????????? ??????
    private Long reviseTargetId;

    //nullable
    @OneToOne
    @JoinColumn(name = "revise_id")
    private NewItem reviseTargetNewItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "modifier_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member modifier;

    @OneToMany(
            mappedBy = "newItem",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<NewItemAttachment> attachments;

    /**
     * ??? ???????????? ?????? ?????? ????????? ??????
     */
    @OneToMany(
            mappedBy = "parent",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<NewItemParentChildren> parent;

    /**
     * ??? ???????????? ?????? ?????? ????????? ??????
     */
    @OneToMany(
            mappedBy = "children",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    //@JoinColumn(name = "children_id")
    private Set< NewItemParentChildren> children;

    @OneToMany(
            mappedBy = "newItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<CoNewItem> coNewItems;

    //0717 ?????? ???????????? ??? ??????????????? ???????????? ??????
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @OneToMany(
            mappedBy = "newItem",
            cascade = CascadeType.ALL,//??????
            orphanRemoval = true, //????????? ?????? ?????????...
            fetch = FetchType.LAZY
    )
    private List<NewItemMember> editors;

    /**
     * attachment ?????? ???, thumbnail ?????? ??? ?????????
     * @param classification
     * @param name
     * @param itemTypes
     * @param itemNumber
     * @param thumbnail
     * @param sharing
     * @param carType
     * @param integrate
     * @param curve
     * @param width
     * @param height
     * @param thickness
     * @param weight
     * @param importance
     * @param color
     * @param loadQuantity
     * @param forming
     * @param coatingWay
     * @param coatingType
     * @param modulus
     * @param screw
     * @param cuttingType
     * @param lcd
     * @param displaySize
     * @param screwHeight
     * @param clientOrganizations
     * @param supplierOrganization
     * @param makers
     * @param partnumbers
     * @param member
     * @param tempsave
     * @param revise_progress
     * @param attachments
     */
    public NewItem(
            Classification classification,
            String name,
            ItemTypes itemTypes,
            String itemNumber,
            NewItemImage thumbnail,
            boolean sharing,
            CarType carType,
            String integrate,
            String curve,
            String width,
            String height,
            String thickness,
            String weight,
            String importance,
            Color color,
            String loadQuantity,
            String forming,
            CoatingWay coatingWay,
            CoatingType coatingType,
            String modulus,
            String screw,
            String cuttingType,
            String lcd,
            String displaySize,
            String screwHeight,
            ClientOrganization clientOrganizations,
            Supplier supplierOrganization,
            Maker makers,
            //List<String> partnumbers,
            String partnumbers,
            Member member,
            Boolean tempsave,
            Boolean revise_progress,

            List<NewItemAttachment> attachments,

            List<NewItemAttachment> duplicatedAttachments
    ) {

        this.classification = classification;
        this.name = name;

        this.itemTypes = itemTypes;

        this.itemNumber = itemNumber;

        if(thumbnail!=null) { //0615 ????????? ????????? null ??? ?????????, ?????? ????????? ?????? (??????????????? ????????? ?????? ??????)
            this.thumbnail = thumbnail;
            addImages(thumbnail);
        }else{
            this.thumbnail = null;
        }

        this.sharing = sharing;

        this.carType = carType;

        this.integrate = integrate;

        this.curve = curve;

        this.width = width;

        this.height = height;

        this.thickness = thickness;

        this.weight = weight;

        this.importance = importance;

        this.color = color;

        this.loadQuantity = loadQuantity;

        this.forming = forming;

        this.coatingWay = coatingWay;

        this.coatingType = coatingType;

        this.modulus = modulus;

        this.screw = screw;

        this.cuttingType = cuttingType;

        this.lcd = lcd;

        this.displaySize = displaySize;

        this.screwHeight = screwHeight;

        this.clientOrganization = clientOrganizations;

        this.supplierOrganization = supplierOrganization;

        this.makers =
                makers;

        this.partNumber = partnumbers;

        this.tempsave = true;
        this.readonly = false;

        this.revise_progress = revise_progress;

        this.attachments = new ArrayList<>();

        addAttachments(attachments);

        this.revision = 65;

        this.member = member;
        this.modifier = member;

        this.released = 0;

        if(duplicatedAttachments!=null){
            this.attachments
                    .addAll(duplicatedAttachments);
            addAttachments(duplicatedAttachments);
        }

    }


    /**
     * attachment ????????? thumbnail ??? ?????? ??? ?????????
     * @param classification
     * @param name
     * @param itemTypes
     * @param itemNumber
     * @param thumbnail
     * @param sharing
     * @param carType
     * @param integrate
     * @param curve
     * @param width
     * @param height
     * @param thickness
     * @param weight
     * @param importance
     * @param color
     * @param loadQuantity
     * @param forming
     * @param coatingWay
     * @param coatingType
     * @param modulus
     * @param screw
     * @param cuttingType
     * @param lcd
     * @param displaySize
     * @param screwHeight
     * @param clientOrganizations
     * @param supplierOrganization
     * @param makers
     * @param partnumbers
     * @param member
     * @param tempsave
     * @param revise_progress
     */
    public NewItem(
            Classification classification,
            String name,
            ItemTypes itemTypes,
            String itemNumber,
            NewItemImage thumbnail,
            boolean sharing,
            CarType carType,
            String integrate,
            String curve,
            String width,
            String height,
            String thickness,
            String weight,
            String importance,
            Color color,
            String loadQuantity,
            String forming,
            CoatingWay coatingWay,
            CoatingType coatingType,
            String modulus,
            String screw,
            String cuttingType,
            String lcd,
            String displaySize,
            String screwHeight,
            ClientOrganization clientOrganizations,
            Supplier supplierOrganization,
            Maker makers,
            //List<String> partnumbers,
            String partnumbers,
            Member member,
            Boolean tempsave,
            Boolean revise_progress,

            List<NewItemAttachment> duplicatedAttachments

    ) {

        this.classification = classification;
        this.name = name;

        this.itemTypes = itemTypes;

        this.itemNumber = itemNumber;

        if(thumbnail!=null) { //0615 ????????? ????????? null ??? ?????????, ?????? ????????? ?????? (???????????? ??? ????????? ????????????)
            this.thumbnail = thumbnail;
            addImages(thumbnail);
        }else{
            this.thumbnail = null;
        }


        this.sharing = sharing;

        this.carType = carType;

        this.integrate = integrate;

        this.curve = curve;

        this.width = width;

        this.height = height;

        this.thickness = thickness;

        this.weight = weight;

        this.importance = importance;

        this.color = color;

        this.loadQuantity = loadQuantity;

        this.forming = forming;

        this.coatingWay = coatingWay;

        this.coatingType = coatingType;

        this.modulus = modulus;

        this.screw = screw;

        this.cuttingType = cuttingType;

        this.lcd = lcd;

        this.displaySize = displaySize;

        this.screwHeight = screwHeight;

        this.clientOrganization = clientOrganizations;

        this.supplierOrganization = supplierOrganization;

        this.makers = makers;
//                makers.stream().map(
//
//                                //????????? ????????? ????????? ??????
//                                r -> new NewItemMaker(
//                                        this, r, partnumbers.get(makers.indexOf(r)).isBlank()?"":partnumbers.get(makers.indexOf(r))
//                                )
//                        )
//                        .collect(toList());

        this.partNumber = partnumbers;

        this.tempsave = true;

        this.readonly = false;

        this.revise_progress = revise_progress;

        this.revision = 65;

        this.member = member;

        this.modifier = member;

        this.released = 0;

        this.attachments = new ArrayList<>();

        if(duplicatedAttachments!=null){
            this.attachments
                    .addAll(duplicatedAttachments);
            addAttachments(duplicatedAttachments);
        }

    }

    /**
     * ????????? ?????????
     *
     * @param added
     */
    private void addImages(NewItemImage added) {
        added.initNewItem(this);
        this.thumbnail=added;
    }

    /**
     * ????????? attachments
     *
     * @param added
     */
    private void addAttachments(List<NewItemAttachment> added) {
        added.forEach(i -> {
                    attachments.add(i);
                    i.initNewItem(this);
                }
        );
    }

    private void addUpdatedAttachments
            (
                    //NewItemUpdateRequest req,
                    List<Long> newTag,
                    List<String> newComment,
                    List<NewItemAttachment> added,
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        added.stream().forEach(i -> {
                    attachments.add(i);

                    i.initNewItem(this);

                    //
            i.setAttach_comment(
                    newComment.size()==0?
                            " ":
                    newComment.get(//0
                            (added.indexOf(i))
                    ).isBlank()?
                            " ":newComment.get(
                            (added.indexOf(i))
                    )
            );

                    i.setTag(attachmentTagRepository
                            .findById(newTag.get(added.indexOf(i))).
                            orElseThrow(AttachmentTagNotFoundException::new).getName());

                    i.setAttachmentaddress(
                            "src/main/prodmedia/image/" +
                                    sdf1.format(now).substring(0,10)
                                    + "/"
                                    + i.getUniqueName()
                    );

                }
        );

    }



    private void oldUpdatedAttachments
            (
                    //NewItemUpdateRequest req,???
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<NewItemAttachment> olds, // ??? ???????????? ?????? old attachments ??? deleted ?????? ????????? ????????????
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        olds.stream().forEach(i -> {

            i.setAttach_comment(
                    oldComment.size()==0?
                            " ":
                    oldComment.get(
                            (olds.indexOf(i))
                    ).isBlank()?
                            " ":oldComment.get(
                            (olds.indexOf(i))
                    )
            );
                    i.setTag(attachmentTagRepository
                            .findById(oldTag.get(olds.indexOf(i))).
                            orElseThrow(AttachmentTagNotFoundException::new).getName());

                    i.setAttachmentaddress(
                            "src/main/prodmedia/image/" +
                                    sdf1.format(now).substring(0,10)
                                    + "/"
                                    + i.getUniqueName()
                    );

                }
        );

    }

    /**
     * ????????? ????????? ?????? (?????? ?????? ????????? ??????)
     *
     * @param deleted
     */
    private void deleteImages(NewItemImage deleted) {
        this.thumbnail.initNewItem(null); //????????? ????????? null??? ????????? ????????? ?????? ??????(?????? ????????????)
        //this.getThumbnail().initNewItem(null);
        this.setThumbnail(null);
    }

    /**
     * ????????? ????????? ??????
     * (?????? ?????? ????????? ??????)
     * @param deleted
     */
    private void deleteAttachments(List<NewItemAttachment> deleted) {

        // 1) save = false ??? ?????? ?????? ??? ??? ?????????

        for (NewItemAttachment att : deleted){
            if(!att.isSave()){
                this.attachments.remove(att);
                //orphanRemoval=true??? ?????? Post???
                //?????? ????????? ???????????? ?????? ????????? ??? Image???
                // ??????????????????????????? ??????
            }
            // 2) save = true ??? ?????? ?????? ??? ????????? ?????? ??????
            else{
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }

    }

    /**
     * ???????????? ?????? ??? ????????? ?????? ????????????

     * @return
     */
    private NewItemImageUpdatedResult findImageUpdatedResult(MultipartFile addedImageFiles) {
        NewItemImage addedImages
                = convertImageFilesToImages(addedImageFiles);
        return new NewItemImageUpdatedResult(addedImageFiles, addedImages);
    }


//    private List<NewItemImage> convertImageIdsToImages(List<Long> imageIds) {
//        return imageIds.stream()
//                .map(id -> convertImageIdToImage(id))
//                .filter(i -> i.isPresent())
//                .map(i -> i.get())
//                .collect(toList());
//    }

//    private Optional<NewItemImage> convertImageIdToImage(Long id) {
//        return this.thumbnail.stream().filter(i -> i.getId().equals(id)).findAny();
//    }

    private NewItemImage convertImageFilesToImages(MultipartFile imageFile) {
        return new NewItemImage(imageFile.getOriginalFilename());
    }

    /**
     * ???????????? ?????? ??? ????????? ?????? ????????????
     *
     * @return
     */
    private NewItemAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<NewItemAttachment> addedAttachments
                = convertAttachmentFilesToAttachments(addedAttachmentFiles);
        List<NewItemAttachment> deletedAttachments
                = convertAttachmentIdsToAttachments(deletedAttachmentIds);
        addedAttachments.stream().forEach( //06-17 added ??? ????????? ?????? ?????? ???????????????
                i->i.setSave(save)
        );
        return new NewItemAttachmentUpdatedResult(addedAttachmentFiles, addedAttachments, deletedAttachments);
    }


    private List<NewItemAttachment> convertAttachmentIdsToAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(id -> convertAttachmentIdToAttachment(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<NewItemAttachment> convertAttachmentIdToAttachment(Long id) {
        return this.attachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    /**
     * ????????? ???????????? ?????????????????? ??????
     * @param attachmentFiles
     * @return
     */
    private List<NewItemAttachment> convertAttachmentFilesToAttachments(
            List<MultipartFile> attachmentFiles) {

        return attachmentFiles.stream().map(attachmentFile ->
                new NewItemAttachment(
                        attachmentFile.getOriginalFilename()
                )).collect(toList());
    }


    /**
     * ???????????? ?????? ???????????? ????????? ????????? ???????????? ??????
     * ??? ?????? ???????????? ????????? ?????? ?????? ???????????????
     * ????????? ????????? ?????????, ????????? ????????? ??????
     */
    @Getter
    @AllArgsConstructor
    public static class NewItemImageUpdatedResult {
        private MultipartFile addedImageFiles;
        private NewItemImage addedImages;
    }

    /**
     * ???????????? ?????? ???????????? ????????? ????????? ???????????? ??????
     * ??? ?????? ???????????? ????????? ?????? ?????? ???????????????
     * ????????? ?????? ?????????, ????????? ?????? ?????? => ?????? ???????????? ??????
     */
    @Getter
    @AllArgsConstructor
    public static class NewItemAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<NewItemAttachment> addedAttachments;
        private List<NewItemAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private NewItemAttachmentUpdatedResult attachmentUpdatedResult;
        private NewItemImageUpdatedResult imageUpdatedResult;
    }

    public void updateTempsaveWhenMadeRoute() {
        this.tempsave = false;
    }

    public void updateReadOnlyWhenSaved() {
        this.readonly = true;
    }

    // ???????????? complete ?????? ??????, ?????? revision ???  ????????? ???????????? ???????????? ??????.
    public void updateRevision(){this.revision =  this.revision+1;}

    // ????????? revise ??? ?????? target item ??? revision ?????? 1 ??? ????????? ???????????? (????????? ?????? ?????? / ?????? ???????????? ?????? ???)
    public void updateRevision(Integer targetRevision){this.revision =  targetRevision+1;}

    @Getter
    @AllArgsConstructor
    public static class NewItemFileUpdatedResult {
        private NewItemAttachmentUpdatedResult attachmentUpdatedResult;
        private NewItemImageUpdatedResult imageUpdatedResult;
    }

    /**
     * postupdaterequest ????????? update ??????
     *
     * @param req
     * @return ?????? ????????? ?????????
     */
    public NewItemFileUpdatedResult update(
            NewItemUpdateRequest req,
            ColorRepository colorRepository,
            MemberRepository memberRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            SupplierRepository supplierRepository,

            MakerRepository makerRepository,
            ItemTypesRepository itemTypesRepository,
            CoatingWayRepository coatingWayRepository,
            CoatingTypeRepository coatingTypeRepository,
            CarTypeRepository carTypeRepository,
            AttachmentTagRepository attachmentTagRepository,

            Classification1Repository classification1Repository,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<NewItemAttachment> targetAttaches
    ) {

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????

        this.setModifiedAt(LocalDateTime.now());

        this.classification = new Classification(
                classification1Repository.findById(req.getClassification1Id())
                        .orElseThrow(ClassificationNotFoundException::new),
                classification2Repository.findById(req.getClassification2Id())
                        .orElseThrow(ClassificationNotFoundException::new),
                classification3Repository.findById(req.getClassification3Id())
                        .orElseThrow(ClassificationNotFoundException::new)
        );

        //TODO update??? ??? ???????????? ?????? ??? ????????? ?????? ?????? ????????? => fix needed
        //isBlank ??? isNull??? ???????????? ?????? ??? / req ??? ????????????
        this.name = req.getName()==null || req.getName().isBlank() ?
                " " : req.getName();

        this.itemTypes = req.getTypeId() == null ?
                null:
                itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemTypeNotFoundException::new);


        this.sharing = req.getSharing() == null || req.getSharing();

        this.carType =     //???????????? ????????? ?????? ????????????
                req.getCarTypeId() == null ?
                        null:
                        //carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new):
                        //null ????????? ???????????? ???
                        carTypeRepository.findById(req.getCarTypeId()).orElseThrow(CarTypeNotFoundException::new);


        this.integrate = req.getIntegrate().isBlank() ?
                "" : req.getIntegrate();

        this.curve = req.getCurve().isBlank() ?
                "" : req.getCurve();

        this.width = req.getWidth().isBlank() ?
                "" : req.getWidth();

        this.height = req.getHeight().isBlank() ?
                "" : req.getHeight();

        this.thickness = req.getThickness().isBlank() ?
                "" : req.getThickness();

        this.weight = req.getWeight().isBlank() ?
                "" : req.getWeight();

        this.importance = req.getImportance().isBlank() ?
                "" : req.getImportance();

        this.color = req.getColorId() == null ?
                null:                //colorRepository.findById(99999L).orElseThrow(ColorNotFoundException::new)

                colorRepository.findById(req.getColorId())
                        .orElseThrow(ColorNotFoundException::new);

        this.loadQuantity = req.getLoadQuantity().isBlank() ?
                "" : req.getLoadQuantity();

        this.forming = req.getForming()==null || req.getForming().isBlank() ? this.forming : req.getForming();

        this.coatingWay = req.getCoatingWayId() == null ?
                null:
                //coatingWayRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new) :
                coatingWayRepository.findById
                        (req.getCoatingWayId()).orElseThrow(CoatingNotFoundException::new);

        this.coatingType = req.getCoatingTypeId() == null ?
                null:
                //coatingTypeRepository.findById(99999L).orElseThrow(CoatingNotFoundException::new) :
                coatingTypeRepository.findById
                        (req.getCarTypeId()).orElseThrow(CoatingNotFoundException::new);


        this.modulus = req.getModulus()==null||req.getModulus().isBlank()?" ":req.getModulus();

        this.screw = req.getScrew()==null||req.getScrew().isBlank()?" ":req.getScrew();

        this.cuttingType = req.getCuttingType()==null||req.getCuttingType().isBlank()?" ":req.getCuttingType();

        this.lcd = req.getLcd()==null||req.getLcd().isBlank()?" ":req.getLcd();

        this.displaySize = req.getDisplaySize()==null||req.getDisplaySize().isBlank()?" ":req.getDisplaySize();

        this.screwHeight = req.getScrewHeight()==null||req.getScrewHeight().isBlank()?" ":req.getScrewHeight();


        this.clientOrganization = req.getClientOrganizationId() == null ?
                null
//                clientOrganizationRepository.findById(99999L)
//                        .orElseThrow(ClientOrganizationNotFoundException::new)
                :
                clientOrganizationRepository.findById(req.getClientOrganizationId())
                        .orElseThrow(ClientOrganizationNotFoundException::new);

        this.supplierOrganization = req.getSupplierOrganizationId() == null ?
                null
//                supplierRepository.findById(99999L)
//                        .orElseThrow(ProduceOrganizationNotFoundException::new)
                :
                supplierRepository.findById(req.getSupplierOrganizationId())
                        .orElseThrow(SupplierNotFoundException::new);

        this.makers = req.getMakersId()==null?
                null
                //makerRepository.findById(-1L).orElseThrow(MakerNotEmptyException::new)
                :makerRepository.findById(req.getMakersId()).orElseThrow(MakerNotFoundException::new);
//        this.makers =
//                makers.stream().map(
//
//                                //????????? ????????? ????????? ??????
//                                r -> new NewItemMaker(
//                                        this, r, partnumbers.get(makers.indexOf(r))
//                                )
//                        )
//                        .collect(toList());
        this.partNumber = req.getPartnumbers().isBlank()?"":req.getPartnumbers();
        this.tempsave = true;
        this.readonly = false;


        //???????????? ??????

        NewItemAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        false
                );

        if (req.getDeletedAttachments().size() > 0) {
            deleteAttachments(
                    resultAttachment.getDeletedAttachments()
            );
        }

        if(oldTags.size()>0) {
            oldUpdatedAttachments(
                    oldTags,
                    oldComment,
                    targetAttaches,
                    attachmentTagRepository
            );
        }

        if (req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
            addUpdatedAttachments(
                    newTags,
                    newComment,
                    //req,
                    resultAttachment.getAddedAttachments(),
                    attachmentTagRepository
            );
        }


        NewItemFileUpdatedResult fileUpdatedResult = null;

        if (req.getThumbnail()!=null && req.getThumbnail().getSize() > 0) {
            System.out.println("??????????????? ???????????? ??????," +
                    "????????? ??????????????? ?????? ??????! ");

            if (this.thumbnail!=null && (!Objects.equals(this.getThumbnail().getOriginName(), this.thumbnail.getOriginName()))) {
                //?????? ????????? ??????
                System.out.println("?????? ????????? ??????????????? ");

                deleteImages(this.thumbnail);
            }

            NewItemImageUpdatedResult resultImage =
                    findImageUpdatedResult(
                            req.getThumbnail()
                    );

            addImages(new NewItemImage(req.getThumbnail().getOriginalFilename()));
            fileUpdatedResult =
                    new NewItemFileUpdatedResult(resultAttachment, resultImage);


        }
        else {

            fileUpdatedResult =
                    new NewItemFileUpdatedResult(resultAttachment, null);

        }

        this.setModifiedAt(LocalDateTime.now());

        return fileUpdatedResult;

    }

    public NewItemFileUpdatedResult tempEnd(
            NewItemUpdateRequest req,
            ColorRepository colorRepository,
            MemberRepository memberRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            SupplierRepository supplierRepository,
            //NewItemMakerRepository itemMakerRepository,
            MakerRepository makerRepository,
            ItemTypesRepository itemTypesRepository,
            CoatingWayRepository coatingWayRepository,
            CoatingTypeRepository coatingTypeRepository,
            CarTypeRepository carTypeRepository,
            AttachmentTagRepository attachmentTagRepository,

            Classification1Repository classification1Repository,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<NewItemAttachment> targetAttaches
    ) {

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????

        this.setModifiedAt(LocalDateTime.now());

        if(req.getClassification1Id()==null || req.getClassification2Id() ==null || req.getClassification3Id()==null){
            throw new ClassificationRequiredException();
        }

        if(req.getClassification1Id()==99999L || req.getClassification2Id() == 99999L){
            //06-18 ?????? 3??? 99999 ?????? ?????????
            throw new ProperClassificationRequiredException();
        }

        if(req.getClassification1Id()==99999L && req.getClassification2Id() ==99999L && req.getClassification3Id()==99999L){
            throw new ProperClassificationRequiredException();
        }

        this.classification = new Classification(
                classification1Repository.findById(req.getClassification1Id())
                        .orElseThrow(ClassificationNotFoundException::new),
                classification2Repository.findById(req.getClassification2Id())
                        .orElseThrow(ClassificationNotFoundException::new),
                classification3Repository.findById(req.getClassification3Id())
                        .orElseThrow(ClassificationNotFoundException::new)
        );

        //????????? ?????? ??????
        if(req.getTypeId()==null){
            throw new ItemTypeRequiredException();
        }

        //????????? ?????? ??????
        if(req.getName().isBlank()){
            throw new ItemNameRequiredException();
        }
        AtomicInteger k = new AtomicInteger();

        //TODO 0614 update??? ??? ???????????? ?????? ??? ????????? ?????? ?????? ????????? => fix needed,
        //TODO ITEMTYPE ????????? 99999??? ?????????
        //isBlank ??? isNull??? ???????????? null / ?????? ??? / req ??? ????????????
        this.name = req.getName().isBlank()?"":req.getName();

        this.itemTypes = req.getTypeId()==null?this.itemTypes:
                itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemTypeNotFoundException::new);

        this.itemNumber =
                req.getClassification1Id() + String.valueOf(ItemType.valueOf(
                        itemTypesRepository.findById(req.getTypeId()).get().getItemType().name()
                ).label() * 1000000 + (int) (Math.random() * 1000));


        this.sharing = req.getSharing();

        this.carType =                 //????????? ?????? ?????? ??????
                (!req.getSharing())?
                        //1. ???????????????
                        req.getCarTypeId()==null?
                                //1-1 : ????????? ????????? (????????? ?????? ????????????
                                carTypeRepository.findById(0L).orElseThrow(CarTypeNotFoundException::new):
                                //null ????????? ???????????? ???
                                carTypeRepository.findById(req.getCarTypeId()).orElseThrow(CarTypeNotFoundException::new)

                        :
                        //2. ???????????????
                        null;
        //carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new);


        this.integrate = req.getIntegrate().isBlank()?" ":req.getIntegrate();

        this.curve = req.getCurve().isBlank()?" ":req.getCurve();

        this.width = req.getWidth().isBlank()?" ":req.getWidth();

        this.height= req.getHeight().isBlank()?" ":req.getHeight();

        this.thickness = req.getThickness().isBlank()?" ":req.getThickness();

        this.weight = req.getWeight().isBlank()?" ":req.getWeight();

        this.importance = req.getImportance().isBlank()?this.importance:req.getImportance();


        this.color = req.getColorId()==null?this.color:colorRepository.findById(req.getColorId())
                .orElseThrow(ColorNotFoundException::new);

        this.loadQuantity = req.getLoadQuantity()==null||req.getLoadQuantity().isBlank()?" ":req.getLoadQuantity();

        this.forming = req.getForming()==null||req.getForming().isBlank()?" ":req.getForming();

        this.coatingWay = req.getCoatingWayId()==null?this.coatingWay:
                coatingWayRepository.findById
                        (req.getCoatingWayId()).orElseThrow(CoatingNotFoundException::new);

        this.coatingType = req.getCoatingTypeId()==null?this.coatingType:
                coatingTypeRepository.findById
                        (req.getCoatingTypeId()).orElseThrow(CoatingNotFoundException::new);


        this.modulus = req.getModulus()==null||req.getModulus().isBlank()?" ":req.getModulus();

        this.screw = req.getScrew()==null||req.getScrew().isBlank()?" ":req.getScrew();

        this.cuttingType = req.getCuttingType()==null||req.getCuttingType().isBlank()?" ":req.getCuttingType();

        this.lcd = req.getLcd()==null||req.getLcd().isBlank()?" ":req.getLcd();

        this.displaySize = req.getDisplaySize()==null||req.getDisplaySize().isBlank()?" ":req.getDisplaySize();

        this.screwHeight = req.getScrewHeight()==null||req.getScrewHeight().isBlank()?" ":req.getScrewHeight();

        this.clientOrganization = req.getClientOrganizationId()==null?this.clientOrganization:
                clientOrganizationRepository.findById(req.getClientOrganizationId())
                        .orElseThrow(ClientOrganizationNotFoundException::new);

        this.supplierOrganization = req.getSupplierOrganizationId()==null?this.supplierOrganization:
                supplierRepository.findById(req.getSupplierOrganizationId())
                        .orElseThrow(SupplierNotFoundException::new);

        this.makers = req.getMakersId()==null?//makerRepository.findById(-1L).orElseThrow(MakerNotFoundException::new)
                null
                :makerRepository.findById(req.getMakersId()).orElseThrow(MakerNotFoundException::new);
//        this.makers =
//                makers.stream().map(
//
//                                //????????? ????????? ????????? ??????
//                                r -> new NewItemMaker(
//                                        this, r, partnumbers.get(makers.indexOf(r))
//                                )
//                        )
//                        .collect(toList());
        this.partNumber = req.getPartnumbers().isBlank()?" ":req.getPartnumbers();
        this.tempsave = true;
        this.readonly = true; //0605- ??? ??????????????? ??????, ??? ?????? ?????? false ?????? true??? ?????? !



        //???????????? ??????

        NewItemAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        true
                );

        if (req.getDeletedAttachments().size() > 0) {
            deleteAttachments(
                    resultAttachment.getDeletedAttachments()
            );
        }

        if(oldTags.size()>0) {
            oldUpdatedAttachments(
                    oldTags,
                    oldComment,
                    targetAttaches,
                    attachmentTagRepository
            );
        }

        if (req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
            addUpdatedAttachments(
                    newTags,
                    newComment,
                    //req,
                    resultAttachment.getAddedAttachments(),
                    attachmentTagRepository
            );
        }


        NewItemFileUpdatedResult fileUpdatedResult = null;

        if (req.getThumbnail()!=null && req.getThumbnail().getSize() > 0) {
            System.out.println("??????????????? ???????????? ??????," +
                    "????????? ??????????????? ?????? ??????! ");

            if (this.thumbnail!=null && (!Objects.equals(this.getThumbnail().getOriginName(), this.thumbnail.getOriginName()))) {
                //?????? ????????? ??????
                System.out.println("?????? ????????? ??????????????? ");

                deleteImages(this.thumbnail);
            }

            NewItemImageUpdatedResult resultImage =
                    findImageUpdatedResult(
                            req.getThumbnail()
                    );

            addImages(new NewItemImage(req.getThumbnail().getOriginalFilename()));
            fileUpdatedResult =
                    new NewItemFileUpdatedResult(resultAttachment, resultImage);


        }
        else {

            fileUpdatedResult =
                    new NewItemFileUpdatedResult(resultAttachment, null);

        }

        this.setModifiedAt(LocalDateTime.now());

        return fileUpdatedResult;


    }

    public void setParent(Set<NewItemParentChildren> parent) {
        this.parent = parent;
    }

    public void setChildren(Set<NewItemParentChildren> children) {
        this.children = children;
    }

    public void newItem_revise_progress_done_when_co_confirmed() {
        this.revise_progress = false;
    }

//    public NewItemCreateResponse register_target_revise_item(Long targetId){
//        this.reviseTargetId = targetId;
//
//    return new NewItemCreateResponse(
//            this.id
//    );
//    }

    public NewItemCreateResponse register_target_revise_item(Long targetId){
        this.reviseTargetId = targetId;

        return new NewItemCreateResponse(
                this.id
        );
    }

    public NewItemCreateResponse saving_target_revise_item(NewItem newItem){
        this.reviseTargetNewItem = newItem;

        return new NewItemCreateResponse(
                newItem.id
        );
    }

    public void setReviseTargetNewItem(NewItem reviseTargetNewItem) {
        System.out.println(reviseTargetNewItem.id+"hiiiiiiiiiiiiiiiiii");
        this.reviseTargetNewItem = reviseTargetNewItem;
    }

    /**
     * ?????? ?????? revise ???????????? ????????? revision+1 ??? ?????? revision ??????
     * ??? ????????? released_cnt ??? ????????? ??? released ??? ??????????????????
     * @param revision <- ????????? ??? ?????? revision+1 ?????? ???
     * @param released_cnt <- ????????? ??????, ????????? ????????? ??????
     * @return
     */
    public NewItemCreateResponse updateRevisionAndHeritageReleaseCnt(int revision, int released_cnt){
        this.revision = Math.toIntExact(revision);
        this.released = Math.toIntExact(released_cnt);

        return new NewItemCreateResponse(
                this.id
        );

    }
    public void setReviseTargetId(Long reviseTargetId) {
        this.reviseTargetId = reviseTargetId;
    }

    /**
     * release review ??? approve Route
     * ????????? ??? ??? release ??? ?????? ????????? or CO ??? ?????? ???????????????
     * RELEASE + 1 ?????? ???
     */
    public void updateReleaseCnt(){
        this.released = this.released+1;
    }


    /**
     * editors ??????????????? ??????
     * @param editors
     */
    public void RegisterEditors(List<NewItemMember> editors){
        this.editors.clear();
        this.editors.addAll(editors);
    }

}