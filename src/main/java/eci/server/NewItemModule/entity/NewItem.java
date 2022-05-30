package eci.server.NewItemModule.entity;

import eci.server.ItemModule.dto.item.ItemUpdateRequest;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.*;
import eci.server.NewItemModule.entity.supplier.Maker;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.coating.CoatingType;
import eci.server.NewItemModule.entity.coating.CoatingWay;
import eci.server.NewItemModule.entity.maker.NewItemMaker;
import eci.server.ProjectModule.entity.project.CarType;
import eci.server.ProjectModule.entity.project.ClientOrganization;
import eci.server.ProjectModule.entity.project.ProduceOrganization;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewItem extends EntityDate {
    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
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
            name = "item_types_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTypes itemTypes;

    @Column(nullable = false)
    private Integer itemNumber;

    @OneToMany(
            mappedBy = "newItem",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<NewItemImage> thumbnail;

    /**
     * true면 공용, false면 전용
     */
    @Column
    private boolean share;

    /**
     * share 이 false 면 필수!
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;

    /**
     * 단독/통합 true 면  통합, false면 단독
     */
    @Column
    private boolean integrate;

    /**
    * true면 곡면, false면 평면
     */
    @Column
    private boolean curve;

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
    private Integer loadQuantity;

    //포밍 유무
    @Column
    private boolean forming;

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
    private Integer modulus;

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
    private Integer displaySize;

    //나사경
    @Column
    private Integer screwHeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProduceOrganization supplierOrganization;

    @OneToMany(
            mappedBy = "newItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<NewItemMaker> makers;

    @Column(nullable = false)
    private Boolean tempsave;

    @Column(nullable = false)
    private Boolean revise_progress;

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

    public NewItem(
            Classification classification,
            String name,
            ItemTypes itemTypes,
            Integer itemNumber,
            List<NewItemImage> thumbnail,
            boolean share,
            CarType carType,
            boolean integrate,
            boolean curve,
            String width,
            String height,
            String thickness,
            String weight,
            String importance,
            Color color,
            Integer loadQuantity,
            boolean forming,
            CoatingWay coatingWay,
            CoatingType coatingType,
            Integer modulus,
            String screw,
            String cuttingType,
            String lcd,
            Integer displaySize,
            Integer screwHeight,
            ClientOrganization clientOrganizations,
            ProduceOrganization supplierOrganization,
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

        this.thumbnail = new ArrayList<>();
        addImages(thumbnail);

        this.share = share;

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
                                        this, r, partnumbers.get(makers.indexOf(r))
                                )
                        )
                        .collect(toList());

        this.tempsave = tempsave;

        this.revise_progress = revise_progress;

        this.attachments = new ArrayList<>();
        addAttachments(attachments);

        this.revision = 65;

    }

    /**
     * 추가할 이미지
     *
     * @param added
     */
    private void addImages(List<NewItemImage> added) {
        added.stream().forEach(i -> {
            thumbnail.add(i);
            i.initNewItem(this);
        });
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

    private void addUpdatedAttachments(ItemUpdateRequest req, List<NewItemAttachment> added) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.stream().forEach(i -> {
            attachments.add(i);
            i.initNewItem(this);
            i.setAttach_comment(req.getAddedAttachmentComment().get((added.indexOf(i))));
            i.setTag(req.getAddedTag().get((added.indexOf(i))));
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
    private void deleteImages(List<NewItemImage> deleted) {
        deleted.stream().
                forEach(di ->
                        this.thumbnail.remove(di)
                );
    }


    /**
     * 삭제될 이미지 제거
     * (고아 객체 이미지 제거)
     * @param deleted
     */
    private void deleteAttachments(List<NewItemAttachment> deleted) {
        deleted.stream().forEach(di ->
                        di.setDeleted(true)
                //this.attachments.remove(di)
        );
    }


    /**
     * 압데이트 돼야 할 이미지 정보 만들어줌
     *
     * @param addedImageFiles
     * @param deletedImageIds
     * @return
     */
    private NewItemImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
        List<NewItemImage> addedImages
                = convertImageFilesToImages(addedImageFiles);
        List<NewItemImage> deletedImages
                = convertImageIdsToImages(deletedImageIds);
        return new NewItemImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }


    private List<NewItemImage> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(id -> convertImageIdToImage(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<NewItemImage> convertImageIdToImage(Long id) {
        return this.thumbnail.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<NewItemImage> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile -> new NewItemImage(imageFile.getOriginalFilename())).collect(toList());
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
        private List<MultipartFile> addedImageFiles;
        private List<NewItemImage> addedImages;
        private List<NewItemImage> deletedImages;
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


}
