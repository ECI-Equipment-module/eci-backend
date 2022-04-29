package eci.server.ProjectModule.entity.project;


import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
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
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate startPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate overPeriod;

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
    @JoinColumn(name = "projectType_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectType projectType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectLevel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectLevel projectLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produceOrganization_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProduceOrganization produceOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    //차종
    @Column
    private String carType;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ProjectAttachment> projectAttachments;

    public Project(
            String name,
            String projectNumber,

            LocalDate startPeriod,
            LocalDate overPeriod,

            Item item,
            Member member,
            Boolean tempsave,

            ProjectType projectType,
            ProjectLevel projectLevel,
            ProduceOrganization produceOrganization,

            ClientOrganization clientOrganizations,
            List<ProjectAttachment> projectAttachments,

            String carType

    ) {
        this.name = name;

        this.projectType = projectType;
        this.projectLevel = projectLevel;
        this.projectNumber = projectNumber;

        this.member = member;
        this.tempsave = tempsave;
        this.startPeriod = startPeriod;
        this.overPeriod = overPeriod;

        this.produceOrganization = produceOrganization;
        this.clientOrganization = clientOrganizations;

        this.item = item;
        this.carType = carType;

        this.projectAttachments = new ArrayList<>();
        addProjectAttachments(projectAttachments);

    }

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

    /**
     * 추가할 attachments
     *
     * @param added
     */
    private void addProjectAttachments(List<ProjectAttachment> added) {
        added.stream().forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);
        });
    }

    /**
     * 삭제될 이미지 제거 (고아 객체 이미지 제거)
     *
     * @param deleted
     */
    private void deleteProjectAttachments(List<ProjectAttachment> deleted) {
        deleted.stream().
                forEach(di ->
                        this.projectAttachments.remove(di)
                );
    }



    /**
     * 업데이트 돼야 할 이미지 정보 만들어줌
     *
     * @return
     */
    private ProjectAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds
    ) {
        List<ProjectAttachment> addedAttachments
                = convertProjectAttachmentFilesToProjectAttachments(addedAttachmentFiles);
        List<ProjectAttachment> deletedAttachments
                = convertProjectAttachmentIdsToProjectAttachments(deletedAttachmentIds);
        return new ProjectAttachmentUpdatedResult(addedAttachmentFiles, addedAttachments, deletedAttachments);
    }


    private List<ProjectAttachment> convertProjectAttachmentIdsToProjectAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(id -> convertProjectAttachmentIdToProjectAttachment(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<ProjectAttachment> convertProjectAttachmentIdToProjectAttachment(Long id) {
        return this.projectAttachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<ProjectAttachment> convertProjectAttachmentFilesToProjectAttachments(List<MultipartFile> attachmentFiles) {
        return attachmentFiles.stream().map(attachmentFile -> new ProjectAttachment(
                attachmentFile.getOriginalFilename()
        )).collect(toList());
    }

    /**
     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
     * 이 정보 기반으로 유저는 실제 파일 저장소에서
     * 추가될 파일 업로드, 삭제할 파일 삭제 => 내역 남아있게 하기
     */
    @Getter
    @AllArgsConstructor
    public static class ProjectAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<ProjectAttachment> addedAttachments;
        private List<ProjectAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private ProjectAttachmentUpdatedResult attachmentUpdatedResult;
    }
}
