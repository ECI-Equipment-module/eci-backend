package eci.server.ProjectModule.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.dto.item.ItemUpdateRequest;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.entity.material.ItemMaterial;
import eci.server.ItemModule.entity.material.Material;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.ColorNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends EntityDate {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String projectNumber;
    //save 할 시에 type + id 값으로 지정

    //@DateTimeFormat(pattern = "yyyy-MM-dd") -> request로 받아올 때 이와 같이 받아오기
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate overPeriod;

    @Column(nullable = false)
    private ProjectType projectType;

    @Column(nullable = false)
    private ProjectLevel projectLevel;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(nullable = false)
    private Boolean tempsave;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Color color;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produceOrganization_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProduceOrganization produceOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    //차종
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private String carType;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ProjectAttachment> projectAttachments;
//
//    public Project(
//            String name,
//            String type,
//            String projectNumber,
//            String width,
//            String height,
//            String weight,
//            Member member,
//            Boolean tempsave,
//            Boolean revise_progress,
//
//            Color color,
//
//            List<Image> thumbnail,
//
//            List<Attachment> attachments,
//
//            List<Material> materials,
//
//            List<Manufacture> manufactures,
//            List<String> partnumbers
//
//    ) {
//        this.name = name;
//        this.type = type;
//        this.itemNumber = itemNumber;
//        this.width = width;
//        this.height = height;
//        this.member = member;
//        this.weight = weight;
//        this.tempsave = tempsave;
//        this.revise_progress = revise_progress;
//
//        this.color = (color);
//
//        this.thumbnail = new ArrayList<>();
//        addImages(thumbnail);
//
//        this.attachments = new ArrayList<>();
//        addAttachments(attachments);
//
//        this.materials =
//                materials.stream().map(
//                                r -> new ItemMaterial(
//                                        this, r)
//                        )
//                        .collect(toList());
//
//        this.manufactures =
//                manufactures.stream().map(
//
//                                //다대다 관계를 만드는 구간
//                                r -> new ItemManufacture(
//                                        this, r, partnumbers.get(manufactures.indexOf(r))
//                                )
//                        )
//                        .collect(toList());
//
//    }
//
//    /**
//     * postupdaterequest 받아서 update 수행
//     *
//     * @param req
//     * @return 새로 수정된 이미지
//     */
//    public eci.server.ItemModule.entity.item.Item.FileUpdatedResult update(
//            ItemUpdateRequest req,
//            ColorRepository colorRepository
//    ) {
//
//        this.name = req.getName();
//        this.type = req.getType();
//        this.width = req.getWidth();
//        this.height = req.getHeight();
//        this.weight = req.getWeight();
//
//        this.color = colorRepository.findById(Long.valueOf(req.getColorId()))
//                .orElseThrow(ColorNotFoundException::new);
//
//        eci.server.ItemModule.entity.item.Item.ImageUpdatedResult resultImage =
//                findImageUpdatedResult(
//                        req.getAddedImages(),
//                        req.getDeletedImages()
//                );
//
//        addImages(resultImage.getAddedImages());
//        deleteImages(resultImage.getDeletedImages());
//
//        eci.server.ItemModule.entity.item.Item.AttachmentUpdatedResult resultAttachment =
//
//                findAttachmentUpdatedResult(
//                        req.getAddedAttachments(),
//                        req.getDeletedAttachments()
//                );
//
//        addAttachments(resultAttachment.getAddedAttachments());
//        deleteAttachments(resultAttachment.getDeletedAttachments());
//
//        eci.server.ItemModule.entity.item.Item.FileUpdatedResult fileUpdatedResult = new eci.server.ItemModule.entity.item.Item.FileUpdatedResult(resultAttachment,resultImage);
//
//        return fileUpdatedResult;
//    }
//
//    /**
//     * 추가할 이미지
//     *
//     * @param added
//     */
//    private void addImages(List<Image> added) {
//        added.stream().forEach(i -> {
//            thumbnail.add(i);
//            i.initItem(this);
//        });
//    }
//
//    /**
//     * 추가할 attachments
//     *
//     * @param added
//     */
//    private void addAttachments(List<Attachment> added) {
//        added.stream().forEach(i -> {
//            attachments.add(i);
//            i.initItem(this);
//        });
//    }
//
//    /**
//     * 삭제될 이미지 제거 (고아 객체 이미지 제거)
//     *
//     * @param deleted
//     */
//    private void deleteImages(List<Image> deleted) {
//        deleted.stream().
//                forEach(di ->
//                        this.thumbnail.remove(di)
//                );
//    }
//
//
//    /**
//     * 삭제될 이미지 제거
//     * (고아 객체 이미지 제거)
//     * @param deleted
//     */
//    private void deleteAttachments(List<Attachment> deleted) {
//        deleted.stream().forEach(di ->
//                        di.setDeleted(true)
//                //this.attachments.remove(di)
//        );
//    }
//
//
//    /**
//     * 압데이트 돼야 할 이미지 정보 만들어줌
//     *
//     * @param addedImageFiles
//     * @param deletedImageIds
//     * @return
//     */
//    private eci.server.ItemModule.entity.item.Item.ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
//        List<Image> addedImages
//                = convertImageFilesToImages(addedImageFiles);
//        List<Image> deletedImages
//                = convertImageIdsToImages(deletedImageIds);
//        return new eci.server.ItemModule.entity.item.Item.ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
//    }
//
//
//    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
//        return imageIds.stream()
//                .map(id -> convertImageIdToImage(id))
//                .filter(i -> i.isPresent())
//                .map(i -> i.get())
//                .collect(toList());
//    }
//
//    private Optional<Image> convertImageIdToImage(Long id) {
//        return this.thumbnail.stream().filter(i -> i.getId().equals(id)).findAny();
//    }
//
//    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
//        return imageFiles.stream().map(imageFile -> new Image(imageFile.getOriginalFilename())).collect(toList());
//    }
//
//    /**
//     * 업데이트 돼야 할 이미지 정보 만들어줌
//     *
//     * @return
//     */
//    private eci.server.ItemModule.entity.item.Item.AttachmentUpdatedResult findAttachmentUpdatedResult(
//            List<MultipartFile> addedAttachmentFiles,
//            List<Long> deletedAttachmentIds
//    ) {
//        List<Attachment> addedAttachments
//                = convertAttachmentFilesToAttachments(addedAttachmentFiles);
//        List<Attachment> deletedAttachments
//                = convertAttachmentIdsToAttachments(deletedAttachmentIds);
//        return new eci.server.ItemModule.entity.item.Item.AttachmentUpdatedResult(addedAttachmentFiles, addedAttachments, deletedAttachments);
//    }
//
//
//    private List<Attachment> convertAttachmentIdsToAttachments(List<Long> attachmentIds) {
//        return attachmentIds.stream()
//                .map(id -> convertAttachmentIdToAttachment(id))
//                .filter(i -> i.isPresent())
//                .map(i -> i.get())
//                .collect(toList());
//    }
//
//    private Optional<Attachment> convertAttachmentIdToAttachment(Long id) {
//        return this.attachments.stream().filter(i -> i.getId().equals(id)).findAny();
//    }
//
//    private List<Attachment> convertAttachmentFilesToAttachments(List<MultipartFile> attachmentFiles) {
//        return attachmentFiles.stream().map(attachmentFile -> new Attachment(
//                attachmentFile.getOriginalFilename()
//        )).collect(toList());
//    }
//
//
//    /**
//     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
//     * 이 정보 기반으로 유저는 실제 파일 저장소에서
//     * 추가될 이미지 업로드, 삭제할 이미지 삭제
//     */
//    @Getter
//    @AllArgsConstructor
//    public static class ImageUpdatedResult {
//        private List<MultipartFile> addedImageFiles;
//        private List<Image> addedImages;
//        private List<Image> deletedImages;
//    }
//
//    /**
//     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
//     * 이 정보 기반으로 유저는 실제 파일 저장소에서
//     * 추가될 파일 업로드, 삭제할 파일 삭제 => 내역 남아있게 하기
//     */
//    @Getter
//    @AllArgsConstructor
//    public static class AttachmentUpdatedResult {
//        private List<MultipartFile> addedAttachmentFiles;
//        private List<Attachment> addedAttachments;
//        private List<Attachment> deletedAttachments;
//    }
//
//    @Getter
//    @AllArgsConstructor
//    public static class FileUpdatedResult {
//        private eci.server.ItemModule.entity.item.Item.AttachmentUpdatedResult attachmentUpdatedResult;
//        private eci.server.ItemModule.entity.item.Item.ImageUpdatedResult imageUpdatedResult;
//    }
}
