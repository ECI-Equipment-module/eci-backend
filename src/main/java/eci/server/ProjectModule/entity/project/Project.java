package eci.server.ProjectModule.entity.project;


import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ProjectModule.dto.project.ProjectUpdateRequest;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.*;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends EntityDate {
    @Id

  @GeneratedValue(strategy = GenerationType.IDENTITY)
//   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
//   @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String projectNumber;
    //save 할 시에 type + id 값으로 지정

    //@DateTimeFormat(pattern = "yyyy-MM-dd") -> request로 받아올 때 이와 같이 받아오기
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate protoStartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate protoOverPeriod;

    @Column(nullable = false)
    private LocalDate p1StartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate p1OverPeriod;

    @Column(nullable = false)
    private LocalDate p2StartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate p2OverPeriod;

    @Column(nullable = false)
    private LocalDate sopStartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate sopOverPeriod;

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

    @Column(nullable = false)
    private Boolean readonly; //05-12반영

    @Column(nullable = false)
    private String lifecycle;

    @Column
    private String clientItemNumber;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ProjectAttachment> projectAttachments;

    @Column
    private char revision;



    public Project(
            String name,
            String projectNumber,
            String clientItemNumber,

            LocalDate protoStartPeriod,
            LocalDate protoOverPeriod,

            LocalDate p1StartPeriod,
            LocalDate p1OverPeriod,

            LocalDate p2StartPeriod,
            LocalDate p2OverPeriod,

            LocalDate sopStartPeriod,
            LocalDate sopOverPeriod,

            Item item,
            Member member,

            Boolean tempsave,
            Boolean readonly,

            ProjectType projectType,
            ProjectLevel projectLevel,
            ProduceOrganization produceOrganization,

            ClientOrganization clientOrganizations,
            List<ProjectAttachment> projectAttachments,

            CarType carType

    ) {
        this.name = name;

        this.projectType = projectType;
        this.projectLevel = projectLevel;
        this.projectNumber = projectNumber;

        this.member = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.protoStartPeriod = protoStartPeriod;
        this.protoOverPeriod = protoOverPeriod;

        this.p1StartPeriod = p1StartPeriod;
        this.p1OverPeriod = p1OverPeriod;

        this.p2StartPeriod = p2StartPeriod;
        this.p2OverPeriod = p2OverPeriod;

        this.sopStartPeriod = sopStartPeriod;
        this.sopOverPeriod = sopOverPeriod;

        this.produceOrganization = produceOrganization;
        this.clientOrganization = clientOrganizations;

        this.item = item;
        this.carType = carType;

        this.projectAttachments = new ArrayList<>();
        addProjectAttachments(projectAttachments);

        this.revision = (char)65;
        this.lifecycle = "WORKING";

        this.clientItemNumber = clientItemNumber;

    }


    /**
     * Project에 Attachment 존재하지 않을 시에 생성자입니다.
     * @param name
     * @param projectNumber
     * @param clientItemNumber
     * @param protoStartPeriod
     * @param protoOverPeriod
     * @param p1StartPeriod
     * @param p1OverPeriod
     * @param p2StartPeriod
     * @param p2OverPeriod
     * @param sopStartPeriod
     * @param sopOverPeriod
     * @param item
     * @param member
     * @param tempsave
     * @param readonly
     * @param projectType
     * @param projectLevel
     * @param produceOrganization
     * @param clientOrganizations
     * @param carType
     */

    public Project(
            String name,
            String projectNumber,
            String clientItemNumber,

            LocalDate protoStartPeriod,
            LocalDate protoOverPeriod,

            LocalDate p1StartPeriod,
            LocalDate p1OverPeriod,

            LocalDate p2StartPeriod,
            LocalDate p2OverPeriod,

            LocalDate sopStartPeriod,
            LocalDate sopOverPeriod,

            Item item,
            Member member,

            Boolean tempsave,
            Boolean readonly,

            ProjectType projectType,
            ProjectLevel projectLevel,
            ProduceOrganization produceOrganization,

            ClientOrganization clientOrganizations,

            CarType carType

    ){
        this.name = name;

        this.projectType = projectType;
        this.projectLevel = projectLevel;
        this.projectNumber = projectNumber;

        this.member = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.protoStartPeriod = protoStartPeriod;
        this.protoOverPeriod = protoOverPeriod;

        this.p1StartPeriod = p1StartPeriod;
        this.p1OverPeriod = p1OverPeriod;

        this.p2StartPeriod = p2StartPeriod;
        this.p2OverPeriod = p2OverPeriod;

        this.sopStartPeriod = sopStartPeriod;
        this.sopOverPeriod = sopOverPeriod;


        this.produceOrganization = produceOrganization;
        this.clientOrganization = clientOrganizations;

        this.item = item;
        this.carType = carType;


        this.revision = (char)65;
        this.lifecycle = "WORKING";

        this.clientItemNumber = clientItemNumber;

    }


    /**
     * 추가할 attachments
     *
     */
    private void addProjectAttachments(List<ProjectAttachment> added) {
        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);
        });
    }


    public FileUpdatedResult update(
            ProjectUpdateRequest req,
            ItemRepository itemRepository,
            ProjectTypeRepository projectTypeRepository,
            ProjectLevelRepository projectLevelRepository,
            ProduceOrganizationRepository produceOrganizationRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository
    )

    {

        this.name = req.getName().isBlank() ? this.name : req.getName();

        this.projectType =
                req.getProjectTypeId() ==null?
                        this.projectType:
                        projectTypeRepository.findById(req.getProjectTypeId())
                .orElseThrow(ProjectTypeNotFoundException::new);

        this.protoStartPeriod =
                req.getStartPeriod().isBlank()?
                        this.protoStartPeriod:
                        LocalDate.parse(req.getStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.protoOverPeriod =
                req.getOverPeriod().isBlank()?
                        this.protoOverPeriod:
                        LocalDate.parse(req.getOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1StartPeriod =
                req.getStartPeriod().isBlank()?
                        this.p1StartPeriod:
                        LocalDate.parse(req.getStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1OverPeriod =
                req.getOverPeriod().isBlank()?
                        this.p1OverPeriod:
                        LocalDate.parse(req.getOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2StartPeriod =
                req.getStartPeriod().isBlank()?
                        this.p2StartPeriod:
                        LocalDate.parse(req.getStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2OverPeriod =
                req.getOverPeriod().isBlank()?
                        this.protoOverPeriod:
                        LocalDate.parse(req.getOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopStartPeriod =
                req.getStartPeriod().isBlank()?
                        this.sopStartPeriod:
                        LocalDate.parse(req.getStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopOverPeriod =
                req.getOverPeriod().isBlank()?
                        this.sopOverPeriod:
                        LocalDate.parse(req.getOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.item =
                req.getItemId()==null?
                        this.item:
                        itemRepository.findById(req.getItemId())
                                .orElseThrow(ItemNotFoundException::new);

        this.projectLevel =
                req.getProjectLevelId() == null?
                        this.projectLevel:
                        projectLevelRepository.findById(req.getProjectLevelId())
                                .orElseThrow(ProjectLevelNotFoundException::new);

        this.clientOrganization =
                req.getClientOrganizationId() == null?
                        this.clientOrganization:
                        clientOrganizationRepository.findById(2L)//req.getProjectLevelId())
                                .orElseThrow(ClientOrganizationNotFoundException::new);

        this.produceOrganization =
                req.getProduceOrganizationId() == null?
                        this.produceOrganization:
                        produceOrganizationRepository.findById(req.getProduceOrganizationId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new);

        this.carType =
                req.getCarType() == null?
                        this.carType:
                        carTypeRepository.findById(req.getCarType())
                                .orElseThrow(CarTypeNotFoundException::new);


        ProjectAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments()
                );
        addUpdatedProjectAttachments(req, resultAttachment.getAddedAttachments());
        //addProjectAttachments(resultAttachment.getAddedAttachments());
        deleteProjectAttachments(resultAttachment.getDeletedAttachments());

        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment//, updatedAddedProjectAttachmentList
        );

        this.clientItemNumber = req.getClientItemNumber().isBlank() ?
                this.clientItemNumber : req.getClientItemNumber();


        return fileUpdatedResult;
    }

    private void addUpdatedProjectAttachments(ProjectUpdateRequest req, List<ProjectAttachment> added) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);

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
    private void deleteProjectAttachments(List<ProjectAttachment> deleted) {
        deleted.
                forEach(di ->
                        this.projectAttachments.remove(di)
                );
    }

    /**
     * 업데이트 돼야 할 파일 정보 만들어줌
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
                .map(this::convertProjectAttachmentIdToProjectAttachment)
                .filter(Optional::isPresent)
                .map(Optional::get)
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

    public void finalSaveProject(){
        //라우트까지 만들어져야 temp save 가 비로소 true
        this.tempsave = false;
    }
}
