package eci.server.NewItemModule.entity;

import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ColorNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.NewItemModule.entity.supplier.Maker;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.coating.CoatingType;
import eci.server.NewItemModule.entity.coating.CoatingWay;
import eci.server.NewItemModule.entity.maker.NewItemMaker;
import eci.server.NewItemModule.entity.supplier.Supplier;
import eci.server.NewItemModule.exception.CoatingNotFoundException;
import eci.server.NewItemModule.exception.SupplierNotFoundException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.exception.*;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import eci.server.NewItemModule.repository.maker.NewItemMakerRepository;
import eci.server.NewItemModule.repository.supplier.SupplierRepository;
import eci.server.ProjectModule.entity.project.CarType;
import eci.server.ProjectModule.entity.project.ClientOrganization;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
            name = "itemTypes_id",
            //name = "item_types_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTypes itemTypes;

    @Column(nullable = false)
    private String itemNumber;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "newItem"
    ) //이렇게 관계 설정해놓으면
    // Item과 NewItemImage간의 fk 가 null이 되면
    // (관계 끊어지면) image 삭제
    private NewItemImage thumbnail;

    /**
     * true면 공용, false면 전용
     */
    @Column
    private boolean sharing;

    /**
     * sharing 이 false 면 필수!
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;

    /**
     * 단독/통합 true 면  통합, false면 단독
     */
    @Column
    private String integrate;

    /**
    * true면 곡면, false면 평면
     */
    @Column
    private String curve;

    //가로
    @Column
    private String width;

    //세로
    @Column
    private String height;

    //두께
    @Column
    private String thickness;

    //중량
    @Column
    private String weight;

    //비중
    @Column
    private String importance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Color color;

    //적재 수량
    @Column
    private String loadQuantity;

    //포밍 유무
    @Column
    private String forming;

    //코팅 방식
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coating_way_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoatingWay coatingWay;

    //코팅 종류
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coating_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoatingType coatingType;

    //모듈러스 숫자 입력
    @Column
    private String modulus;

    //나사 (태평 , 기계)
    @Column
    private String screw;

    //커팅방식 : 레이저, 타발
    @Column
    private String cuttingType;

    //LCD : TDT , SEG, OLED
    @Column
    private String lcd;

    //Display 사이즈
    @Column
    private String displaySize;

    //나사경
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

    @OneToMany(
            mappedBy = "newItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<NewItemMaker> makers;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "supplier_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<NewItemMaker> makers;

    @Column(nullable = false)
    private boolean tempsave;

    @Column
    private boolean readonly;

    @Column//0613 추가 - CAD에서 추가 속성, 디폴트가 false
    private boolean subAssy;

    @Column(nullable = false)
    private boolean revise_progress;

    @Column
    private int revision;

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
     * 한 아이템은 여러 부모 가지기 가능
     */
    @OneToMany(
            mappedBy = "parent",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<NewItemParentChildren> parent;

    /**
     * 한 아이템은 여러 자식 가지기 가능
     */
    @OneToMany(
            mappedBy = "children",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    //@JoinColumn(name = "children_id")
    private List< NewItemParentChildren> children;
    /**
     * attachment 있을 때 생성자
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
            List<Maker> makers,
            List<String> partnumbers,

            Member member,
            Boolean tempsave,
            Boolean revise_progress,

            List<NewItemAttachment> attachments
    ) {

        this.classification = classification;
        this.name = name;

        this.itemTypes = itemTypes;

        this.itemNumber = itemNumber;

        this.thumbnail = thumbnail;
        addImages(thumbnail);

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
                makers.stream().map(

                                //다대다 관계를 만드는 구간
                                r -> new NewItemMaker(
                                        this, r, partnumbers.get(makers.indexOf(r)).isBlank()?"":partnumbers.get(makers.indexOf(r))
                                )
                        )
                        .collect(toList());

        this.tempsave = true;
        this.readonly = false;

        this.revise_progress = revise_progress;

        this.attachments = new ArrayList<>();
        addAttachments(attachments);

        this.revision = 65;

        this.member = member;
        this.modifier = member;

    }


    /**
     * attachment 없을 때 생성자
     * @param classification
     * @param name
     * @param itemTypes
     * @param itemNumber
     * @param thumbnail
     * @param sharinge
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
            boolean sharinge,
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
            List<Maker> makers,
            List<String> partnumbers,

            Member member,
            Boolean tempsave,
            Boolean revise_progress

    ) {

        this.classification = classification;
        this.name = name;

        this.itemTypes = itemTypes;

        this.itemNumber = itemNumber;

        this.thumbnail = thumbnail;
        addImages(thumbnail);

        this.sharing = sharinge;

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
                makers.stream().map(

                                //다대다 관계를 만드는 구간
                                r -> new NewItemMaker(
                                        this, r, partnumbers.get(makers.indexOf(r)).isBlank()?"":partnumbers.get(makers.indexOf(r))
                                )
                        )
                        .collect(toList());

        this.tempsave = true;

        this.readonly = false;

        this.revise_progress = revise_progress;

        this.revision = 65;

        this.member = member;
        this.modifier = member;

    }


    /**
     * 추가할 이미지
     *
     * @param added
     */
    private void addImages(NewItemImage added) {
            added.initNewItem(this);
            this.thumbnail=added;
    }

    /**
     * 추가할 attachments
     *
     * @param added
     */
    private void addAttachments(List<NewItemAttachment> added) {
        added.forEach(i -> {
            attachments.add(i);
            i.initNewItem(this);
        });
    }


    private void addUpdatedAttachments
            (NewItemUpdateRequest req, List<NewItemAttachment> added,
             AttachmentTagRepository attachmentTagRepository) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.stream().forEach(i -> {
            attachments.add(i);
            i.initNewItem(this);

            i.setAttach_comment(req.getAddedAttachmentComment().get((added.indexOf(i))));

            i.setTag(attachmentTagRepository
                    .findById(req.getAddedTag().get(added.indexOf(i))).
                    orElseThrow(AttachmentNotFoundException::new).getName());

//            i.setAttach_comment(req.getAddedAttachmentComment().get((req.getAddedAttachments().indexOf(i))));
//            i.setTag(req.getAddedTag().get((req.getAddedAttachments().indexOf(i))));

            i.setAttachmentaddress(
                    "src/main/prodmedia/image/" +
                            sdf1.format(now).substring(0,10)
                            + "/"
                            + i.getUniqueName()
            );
        });
    }

    /**
     * 삭제될 이미지 제거 (고아 객체 이미지 제거)
     *
     * @param deleted
     */
    private void deleteImages(NewItemImage deleted) {
        this.thumbnail.initNewItem(null); //이렇게 부모를 null로 해주면 알아서 삭제 처리(관계 끊어져서)
        //this.getThumbnail().initNewItem(null);
        this.setThumbnail(null);
    }


    /**
     * 삭제될 이미지 제거
     * (고아 객체 이미지 제거)
     * @param deleted
     */
    private void deleteAttachments(List<NewItemAttachment> deleted) {
        deleted.forEach(di ->
                        di.setDeleted(true)
                //this.attachments.remove(di)
        );
        deleted.forEach(di ->
                        di.setModifiedAt(LocalDateTime.now())
                //this.attachments.remove(di)
        );
    }


    /**
     * 압데이트 돼야 할 이미지 정보 만들어줌

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
     * 업데이트 돼야 할 이미지 정보 만들어줌
     *
     * @return
     */
    private NewItemAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds
    ) {
        List<NewItemAttachment> addedAttachments
                = convertAttachmentFilesToAttachments(addedAttachmentFiles);
        List<NewItemAttachment> deletedAttachments
                = convertAttachmentIdsToAttachments(deletedAttachmentIds);
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

    private List<NewItemAttachment> convertAttachmentFilesToAttachments(List<MultipartFile> attachmentFiles) {
        return attachmentFiles.stream().map(attachmentFile -> new NewItemAttachment(
                attachmentFile.getOriginalFilename()
        )).collect(toList());
    }


    /**
     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
     * 이 정보 기반으로 유저는 실제 파일 저장소에서
     * 추가될 이미지 업로드, 삭제할 이미지 삭제
     */
    @Getter
    @AllArgsConstructor
    public static class NewItemImageUpdatedResult {
        private MultipartFile addedImageFiles;
        private NewItemImage addedImages;
    }

    /**
     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
     * 이 정보 기반으로 유저는 실제 파일 저장소에서
     * 추가될 파일 업로드, 삭제할 파일 삭제 => 내역 남아있게 하기
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



    @Getter
    @AllArgsConstructor
    public static class NewItemFileUpdatedResult {
        private NewItemAttachmentUpdatedResult attachmentUpdatedResult;
        private NewItemImageUpdatedResult imageUpdatedResult;
    }


    /**
     * postupdaterequest 받아서 update 수행
     *
     * @param req
     * @return 새로 수정된 이미지
     */
    public NewItemFileUpdatedResult update(
            NewItemUpdateRequest req,
            ColorRepository colorRepository,
            MemberRepository memberRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            SupplierRepository supplierRepository,
            NewItemMakerRepository itemMakerRepository,
            ItemTypesRepository itemTypesRepository,
            CoatingWayRepository coatingWayRepository,
            CoatingTypeRepository coatingTypeRepository,
            CarTypeRepository carTypeRepository,
            AttachmentTagRepository attachmentTagRepository
    ) {
        this.setModifiedAt(LocalDateTime.now());

        AtomicInteger k = new AtomicInteger();

        //TODO update할 때 사용자가 기존 값 없애고 보낼 수도 있자나 => fix needed
        //isBlank 랑 isNull로 판단해서 기존 값 / req 값 채워넣기
        this.name = req.getName().isBlank() ? this.name : req.getName();

        this.itemTypes = req.getTypeId() == null ? this.itemTypes :
                itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new);


        this.sharing = req.isSharing();

        this.carType =
                this.carType =                 //전용일 때야 차종 생성
                        (!req.isSharing()) ?
                                //1. 전용이라면
                                req.getCarTypeId() == null ?
                                        //1-1 : 아이디 없으면 (무조건 에러 튕기도록
                                        carTypeRepository.findById(0L).orElseThrow(CarTypeNotFoundException::new) :
                                        //null 아니면 입력받은 것
                                        carTypeRepository.findById(req.getCarTypeId()).orElseThrow(CarTypeNotFoundException::new)

                                :
                                //2. 공용이라면
                                carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new);


        this.integrate = req.getIntegrate().isBlank() ? this.integrate : req.getIntegrate();

        this.curve = req.getCurve().isBlank() ? this.curve : req.getCurve();

        this.width = req.getWidth().isBlank() ? this.width : req.getWidth();

        this.height = req.getHeight().isBlank() ? this.height : req.getHeight();

        this.thickness = req.getThickness().isBlank() ? this.thickness : req.getThickness();

        this.weight = req.getWeight().isBlank() ? this.weight : req.getWeight();

        this.importance = req.getImportance().isBlank() ? this.importance : req.getImportance();

        this.color = req.getColorId() == null ? this.color : colorRepository.findById(req.getColorId())
                .orElseThrow(ColorNotFoundException::new);

        this.loadQuantity = req.getLoadQuantity().isBlank() ? this.loadQuantity : req.getLoadQuantity();

        this.forming = req.getForming().isBlank() ? this.forming : req.getForming();

        this.coatingWay = req.getCoatingWayId() == null ? this.coatingWay :
                coatingWayRepository.findById
                        (req.getCoatingWayId()).orElseThrow(CoatingNotFoundException::new);

        this.coatingType = req.getCoatingTypeId() == null ? this.coatingType :
                coatingTypeRepository.findById
                        (req.getCarTypeId()).orElseThrow(CoatingNotFoundException::new);


        this.modulus = req.getModulus().isBlank() ? this.modulus : req.getModulus();

        this.screw = req.getScrew().isBlank() ? this.screw : req.getScrew();

        this.cuttingType = req.getCuttingType().isBlank() ? this.cuttingType : req.getCuttingType();

        this.lcd = req.getLcd().isBlank() ? this.lcd : req.getLcd();

        this.displaySize = req.getDisplaySize().isBlank() ? this.displaySize : req.getDisplaySize();

        this.screwHeight = req.getScrewHeight().isBlank() ? this.screwHeight : req.getScrewHeight();

        this.clientOrganization = req.getClientOrganizationId() == null ? this.clientOrganization :
                clientOrganizationRepository.findById(req.getClientOrganizationId())
                        .orElseThrow(ClientOrganizationNotFoundException::new);

        this.supplierOrganization = req.getSupplierOrganizationId() == null ? this.supplierOrganization :
                supplierRepository.findById(req.getSupplierOrganizationId())
                        .orElseThrow(SupplierNotFoundException::new);

//        this.makers =
//                makers.stream().map(
//
//                                //다대다 관계를 만드는 구간
//                                r -> new NewItemMaker(
//                                        this, r, partnumbers.get(makers.indexOf(r))
//                                )
//                        )
//                        .collect(toList());

        this.tempsave = true;
        this.readonly = false;

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가

        //첨부파일 시작

        NewItemAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments()
                );

        if (req.getAddedTag().size() > 0) {
            addUpdatedAttachments(req, resultAttachment.getAddedAttachments(), attachmentTagRepository);
        }

        if (req.getDeletedAttachments().size() > 0) {
            deleteAttachments(resultAttachment.getDeletedAttachments());
        }

        NewItemFileUpdatedResult fileUpdatedResult = null;

        if (req.getThumbnail().getSize() > 0) {
            System.out.println("업데이트된 썸네일이 있네," +
                    "기존걸 삭제하고ㅡ 이걸 넣자! ");

            if (this.getThumbnail() != null) {
                //기존 이미지 삭제
                System.out.println("기존 이미지 삭제할게잉 ");

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
        return fileUpdatedResult;

    }

    public NewItemFileUpdatedResult tempEnd(
            NewItemUpdateRequest req,
            ColorRepository colorRepository,
            MemberRepository memberRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            SupplierRepository supplierRepository,
            NewItemMakerRepository itemMakerRepository,
            ItemTypesRepository itemTypesRepository,
            CoatingWayRepository coatingWayRepository,
            CoatingTypeRepository coatingTypeRepository,
            CarTypeRepository carTypeRepository,
            AttachmentTagRepository attachmentTagRepository
    ) {

        if(req.getClassification1Id()==null || req.getClassification2Id() ==null || req.getClassification3Id()==null){
            throw new ClassificationRequiredException();
        }
        if(req.getClassification1Id()==99999L || req.getClassification2Id() == 99999L || req.getClassification3Id()== 99999L){
            throw new ProperClassificationRequiredException();
        }
        //아이템 타입 체크
        if(req.getTypeId()==null){
            throw new ItemTypeRequiredException();
        }

        //아이템 이름 체크
        if(req.getName().isBlank()){
            throw new ItemNameRequiredException();
        }
        AtomicInteger k = new AtomicInteger();

        //TODO 0614 update할 때 사용자가 기존 값 없애고 보낼 수도 있자나 => fix needed,
         //TODO ITEMTYPE 없으면 99999면 안되게
        //isBlank 랑 isNull로 판단해서 기존 값 / req 값 채워넣기
        this.name = req.getName().isBlank()?this.name:req.getName();

        this.itemTypes = req.getTypeId()==null?this.itemTypes:
                itemTypesRepository.findById(req.getTypeId()).orElseThrow(ItemNotFoundException::new);

        this.itemNumber =
                req.getClassification1Id() + String.valueOf(ItemType.valueOf(
                        itemTypesRepository.findById(req.getTypeId()).get().getItemType().name()
                ).label() * 1000000 + (int) (Math.random() * 1000));


        this.sharing = req.isSharing();

        this.carType =                 //전용일 때야 차종 생성
                (!req.isSharing())?
                        //1. 전용이라면
                        req.getCarTypeId()==null?
                                //1-1 : 아이디 없으면 (무조건 에러 튕기도록
                                carTypeRepository.findById(0L).orElseThrow(CarTypeNotFoundException::new):
                                //null 아니면 입력받은 것
                                carTypeRepository.findById(req.getCarTypeId()).orElseThrow(CarTypeNotFoundException::new)

                        :
                        //2. 공용이라면
                        carTypeRepository.findById(99999L).orElseThrow(CarTypeNotFoundException::new);


                this.integrate = req.getIntegrate().isBlank()?this.integrate:req.getIntegrate();

        this.curve = req.getCurve().isBlank()?this.curve:req.getCurve();

        this.width = req.getWidth().isBlank()?this.width:req.getWidth();

        this.height= req.getHeight().isBlank()?this.height:req.getHeight();

        this.thickness = req.getThickness().isBlank()?this.thickness:req.getThickness();

        this.weight = req.getWeight().isBlank()?this.weight:req.getWeight();

        this.importance = req.getImportance().isBlank()?this.importance:req.getImportance();


        this.color = req.getColorId()==null?this.color:colorRepository.findById(req.getColorId())
                .orElseThrow(ColorNotFoundException::new);

        this.loadQuantity = req.getLoadQuantity().isBlank()?this.loadQuantity:req.getLoadQuantity();

        this.forming = req.getForming().isBlank()?this.forming:req.getForming();

        this.coatingWay = req.getCoatingWayId()==null?this.coatingWay:
                coatingWayRepository.findById
                        (req.getCoatingWayId()).orElseThrow(CoatingNotFoundException::new);

        this.coatingType = req.getCoatingTypeId()==null?this.coatingType:
                coatingTypeRepository.findById
                        (req.getCarTypeId()).orElseThrow(CoatingNotFoundException::new);


        this.modulus = req.getModulus().isBlank()?this.modulus:req.getModulus();

        this.screw = req.getScrew().isBlank()?this.screw:req.getScrew();

        this.cuttingType = req.getCuttingType().isBlank()?this.cuttingType:req.getCuttingType();

        this.lcd = req.getLcd().isBlank()?this.lcd:req.getLcd();

        this.displaySize = req.getDisplaySize().isBlank()?this.displaySize:req.getDisplaySize();

        this.screwHeight = req.getScrewHeight().isBlank()?this.screwHeight:req.getScrewHeight();

        this.clientOrganization = req.getClientOrganizationId()==null?this.clientOrganization:
                clientOrganizationRepository.findById(req.getClientOrganizationId())
                        .orElseThrow(ClientOrganizationNotFoundException::new);

        this.supplierOrganization = req.getSupplierOrganizationId()==null?this.supplierOrganization:
                supplierRepository.findById(req.getSupplierOrganizationId())
                        .orElseThrow(SupplierNotFoundException::new);

//        this.makers =
//                makers.stream().map(
//
//                                //다대다 관계를 만드는 구간
//                                r -> new NewItemMaker(
//                                        this, r, partnumbers.get(makers.indexOf(r))
//                                )
//                        )
//                        .collect(toList());

        this.tempsave = true;
        this.readonly = true; //0605- 이 부분하나가 변경, 이 것은 얘를 false 에서 true로 변경 !

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가


        NewItemAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments()
                );

        if (req.getAddedTag().size() > 0) {
            addUpdatedAttachments(req, resultAttachment.getAddedAttachments(), attachmentTagRepository);
        }

        if (req.getDeletedAttachments().size() > 0) {
            deleteAttachments(resultAttachment.getDeletedAttachments());
        }

        NewItemFileUpdatedResult fileUpdatedResult = null;

        if (req.getThumbnail().getSize() > 0) {
            System.out.println("업데이트된 썸네일이 있네," +
                    "기존걸 삭제하고ㅡ 이걸 넣자! ");

            if (this.getThumbnail() != null) {
                //기존 이미지 삭제
                System.out.println("기존 이미지 삭제할게잉 ");

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
        return fileUpdatedResult;
    }

    public void setParent(List<NewItemParentChildren> parent) {
        this.parent = parent;
    }

    public void setChildren(List<NewItemParentChildren> children) {
        this.children = children;
    }

}
