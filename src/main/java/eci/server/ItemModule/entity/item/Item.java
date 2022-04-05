package eci.server.ItemModule.entity.item;

import eci.server.ItemModule.dto.item.ItemUpdateRequest;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.route.Route;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer itemNumber;
    //save 할 시에 type + id 값으로 지정

    @Column(nullable = false)
    private String width;

    @Column(nullable = false)
    private String height;

    @Column(nullable = false)
    private String weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(
            mappedBy = "item",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Image> thumbnail;

    private Boolean inProgress;

    public Item(
            String name,
            String type,
            Integer itemNumber,
            String width,
            String height,
            String weight,
            Member member,
            Boolean inProgress,
            List<Image> thumbnail

    ) {
        this.name = name;
        this.type = type;
        this.itemNumber = itemNumber;
        this.width = width;
        this.height = height;
        this.member = member;
        this.weight = weight;
        this.inProgress = inProgress;
        this.thumbnail = new ArrayList<>();
        addImages(thumbnail);
    }

    /**
     * postupdaterequest 받아서 update 수행
     * @param req
     * @return 새로 수정된 이미지
     */
    public ImageUpdatedResult update(ItemUpdateRequest req) {
        this.name = req.getName();
        this.type = req.getType();
        this.width = req.getWidth();
        this.height = req.getHeight();
        this.weight = req.getWeight();
        ImageUpdatedResult result =
                findImageUpdatedResult(
                        req.getAddedImages(),
                        req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }

    /**
     * 추가할 이미지
     * @param added
     */
    private void addImages(List<Image> added) {
        added.stream().forEach(i -> {
            thumbnail.add(i);
            i.initItem(this);
        });
    }

    /**
     * 삭제될 이미지 제거 (고아 객체 이미지 제거)
     * @param deleted
     */
    private void deleteImages(List<Image> deleted) {
        deleted.stream().forEach(di -> this.thumbnail.remove(di));
    }

    /**
     * 압데이트 돼야 할 이미지 정보 만들어줌
     * @param addedImageFiles
     * @param deletedImageIds
     * @return
     */
    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(id -> convertImageIdToImage(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }
    private Optional<Image> convertImageIdToImage(Long id) {
        return this.thumbnail.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile -> new Image(imageFile.getOriginalFilename())).collect(toList());
    }

    /**
     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
     * 이 정보 기반으로 유저는 실제 파일 저장소에서
     * 추가될 이미지 업로드, 삭제할 이미지 삭제
     */
    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult {
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}