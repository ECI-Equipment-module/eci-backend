package eci.server.ProjectModule.entity.project;


import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.ProjectCreateRequest;
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
import java.time.LocalDateTime;
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
    @Column
    private LocalDate protoStartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column
    private LocalDate protoOverPeriod;

    @Column
    private LocalDate p1StartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column
    private LocalDate p1OverPeriod;

    @Column
    private LocalDate p2StartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column
    private LocalDate p2OverPeriod;

    @Column
    private LocalDate sopStartPeriod;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column
    private LocalDate sopOverPeriod;

    @OneToOne
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

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
    @JoinColumn(name = "produceOrganization_id")
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

            NewItem item,
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
        //생성 시에는 수정자 저장하지 않을 것이다.

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

        this.newItem = item;
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

            NewItem item,
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

        this.newItem = item;
        this.carType = carType;


        this.revision = (char)65;
        this.lifecycle = "WORKING";

        this.clientItemNumber = clientItemNumber;

    }


    /**
     * 추가할 attachments
     *
     */
    private void addProjectAttachments(
            List<ProjectAttachment> added) {
        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);
        });
    }


    public FileUpdatedResult update(
            ProjectUpdateRequest req,
            NewItemRepository newItemRepository,
            ProjectTypeRepository projectTypeRepository,
            ProjectLevelRepository projectLevelRepository,
            ProduceOrganizationRepository produceOrganizationRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository
    )

    {

        this.setModifiedAt(LocalDateTime.now());

        this.name = req.getName().isBlank() ? this.name : req.getName();

        this.projectType =
                req.getProjectTypeId() ==null?
                        this.projectType:
                        projectTypeRepository.findById(req.getProjectTypeId())
                .orElseThrow(ProjectTypeNotFoundException::new);

        this.protoStartPeriod =
                req.getProtoStartPeriod() ==null ||
                        req.getProtoStartPeriod().isBlank()?
                        this.protoStartPeriod:
                        LocalDate.parse(req.getProtoStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.protoOverPeriod =
                req.getProtoStartPeriod() ==null ||
                req.getProtoOverPeriod().isBlank()?
                        this.protoOverPeriod:
                        LocalDate.parse(req.getProtoOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1StartPeriod =
                req.getP1StartPeriod() == null ||
                req.getP1StartPeriod().isBlank()?
                        this.p1StartPeriod:
                        LocalDate.parse(req.getP1StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1OverPeriod =
                req.getP1OverPeriod() == null ||
                req.getP1OverPeriod().isBlank()?
                        this.p1OverPeriod:
                        LocalDate.parse(req.getP1OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2StartPeriod =
                req.getP2StartPeriod() ==null ||
                req.getP2StartPeriod().isBlank()?
                        this.p2StartPeriod:
                        LocalDate.parse(req.getP2StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2OverPeriod =
                req.getP2OverPeriod() ==null||
                req.getP2OverPeriod().isBlank()?
                        this.protoOverPeriod:
                        LocalDate.parse(req.getP2OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopStartPeriod =
                req.getSopStartPeriod() ==null||
                req.getSopStartPeriod().isBlank()?
                        this.sopStartPeriod:
                        LocalDate.parse(req.getSopStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopOverPeriod =
                req.getSopOverPeriod() ==null ||
                req.getSopOverPeriod().isBlank()?
                        this.sopOverPeriod:
                        LocalDate.parse(req.getSopOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.newItem =
                req.getItemId()==null?
                        this.newItem:
                        newItemRepository.findById(req.getItemId())
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
                req.getSupplierId() == null?
                        this.produceOrganization:
                        produceOrganizationRepository.findById(req.getSupplierId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new);

        this.carType =
                req.getCarType() == null?
                        this.carType:
                        carTypeRepository.findById(req.getCarType())
                                .orElseThrow(CarTypeNotFoundException::new);


        ProjectAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        false
                );
        if(req.getAddedTag().size()>0) {
            addUpdatedProjectAttachments(
                    req,
                    resultAttachment.getAddedAttachments(),
                     attachmentTagRepository
            );
            //addProjectAttachments(resultAttachment.getAddedAttachments());
        }

        if(req.getDeletedAttachments().size()>0) {
            deleteProjectAttachments(resultAttachment.getDeletedAttachments());
        }

        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment//, updatedAddedProjectAttachmentList
        );

        this.clientItemNumber = req.getClientItemNumber()==null ?
                null : req.getClientItemNumber();

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가


        return fileUpdatedResult;
    }

    //06-17 추가
    public void setTempsave(Boolean tempsave) {
        this.tempsave = tempsave;
    }

    //06-17 추가
    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    private void addUpdatedProjectAttachments(
            ProjectUpdateRequest req,
            List<ProjectAttachment> added,
            AttachmentTagRepository attachmentTagRepository
    ) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);

            i.setAttach_comment(req.getAddedAttachmentComment().get((added.indexOf(i))));
            i.setTag(attachmentTagRepository
                    .findById(req.getAddedTag().get(added.indexOf(i))).
                    orElseThrow(AttachmentNotFoundException::new).getName());
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
//    private void deleteProjectAttachments(List<ProjectAttachment> deleted) {
//        deleted.
//                forEach(di ->
//                        this.projectAttachments.remove(di)
//                );
//    }
    private void deleteProjectAttachments(List<ProjectAttachment> deleted) {
        // 1) save = false 인 애들 지울 땐 찐 지우기

        for (ProjectAttachment att : deleted){
            if(!att.isSave()){
                this.projectAttachments.remove(att);
                //orphanRemoval=true에 의해 Post와
                //연관 관계가 끊어지며 고아 객체가 된 Image는
                // 데이터베이스에서도 제거
            }
            // 2) save = true 인 애들 지울 땐 아래와 같이 진행
            else{
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }
    }

    /**
     * 업데이트 돼야 할 파일 정보 만들어줌
     *
     * @return
     */
    private ProjectAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<ProjectAttachment> addedAttachments
                = convertProjectAttachmentFilesToProjectAttachments(
                        addedAttachmentFiles,
                save);
        List<ProjectAttachment> deletedAttachments
                = convertProjectAttachmentIdsToProjectAttachments(deletedAttachmentIds);

        addedAttachments.stream().forEach( //06-17 added 에 들어온 것은 모두 임시저장용
                i->i.setSave(false)
        );

        return new ProjectAttachmentUpdatedResult(
                addedAttachmentFiles, addedAttachments, deletedAttachments);
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

    /**
     * 어찌저찌
     * @param attachmentFiles
     * @return
     */
    private List<ProjectAttachment> convertProjectAttachmentFilesToProjectAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save) {
        return attachmentFiles.stream().map(attachmentFile ->
                new ProjectAttachment(
                attachmentFile.getOriginalFilename(),
                        save
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
        this.readonly = true;
    }



    public FileUpdatedResult tempEnd(
            ProjectUpdateRequest req,
            NewItemRepository newItemRepository,
            ProjectTypeRepository projectTypeRepository,
            ProjectLevelRepository projectLevelRepository,
            ProduceOrganizationRepository produceOrganizationRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository
    ) {

        this.tempsave = true; //라우트 작성하기 전이니깐 !
        this.readonly = true; //0605- 이 부분하나가 변경, 이 것은 얘를 false 에서 true로 변경 !

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가

        // 수정 시간 갱신
        this.setModifiedAt(LocalDateTime.now());

        this.name = req.getName()==null || req.getName().isBlank() ?
                projectLevelRepository.findById(-1L).orElseThrow(
                        NameNotEmptyException::new)
                        .getName() :
        req.getName();
        this.projectNumber =
                req.getProjectLevelId()==null||req.getProjectLevelId()==99999L?
                        projectLevelRepository.findById(-1L).orElseThrow(ProjectLevelNotEmptyException::new)
                                .getName():
                ProjectCreateRequest.ProjectNumber(req.getProjectLevelId());

        this.protoStartPeriod = req.getProtoStartPeriod()==null || req.getProtoStartPeriod().isBlank()?
                null:LocalDate.parse(req.getProtoStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.protoOverPeriod = req.getProtoOverPeriod()==null || req.getProtoOverPeriod().isBlank()?
                null:LocalDate.parse(req.getProtoOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1StartPeriod = req.getP1StartPeriod()==null || req.getP1StartPeriod().isBlank()?
                null:LocalDate.parse(req.getP1StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1OverPeriod = req.getP1OverPeriod()==null || req.getP1OverPeriod().isBlank()?
                null:LocalDate.parse(req.getP1OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2StartPeriod = req.getP2StartPeriod()==null || req.getP2StartPeriod().isBlank()?
                null:LocalDate.parse(req.getP2StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2OverPeriod = req.getP2OverPeriod()==null || req.getP2OverPeriod().isBlank()?
                null:LocalDate.parse(req.getP2OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopStartPeriod = req.getSopStartPeriod()==null || req.getSopStartPeriod().isBlank()?
                null:LocalDate.parse(req.getSopStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopOverPeriod = req.getSopOverPeriod()==null || req.getSopOverPeriod().isBlank()?
                null:LocalDate.parse(req.getSopOverPeriod(), DateTimeFormatter.ISO_DATE);


        this.newItem =
                req.getItemId()==null?
                        newItemRepository.findById(-1L)
                                .orElseThrow(ItemTypeNotEmptyException::new):
                        newItemRepository.findById(req.getItemId())
                                .orElseThrow(ItemNotFoundException::new);


        this.projectType =
                req.getProjectTypeId() == null?
                        projectTypeRepository.findById(-1L)
                                .orElseThrow(ProjectTypeNotEmptyException::new):
                        projectTypeRepository.findById(req.getProjectTypeId())
                                .orElseThrow(ProjectTypeNotFoundException::new);


        this.projectLevel =
                req.getProjectLevelId() == null?
                        projectLevelRepository.findById(-1L)
                                .orElseThrow(ProjectLevelNotEmptyException::new):
                        projectLevelRepository.findById(req.getProjectLevelId())
                                .orElseThrow(ProjectLevelNotFoundException::new);

        this.clientOrganization =
                req.getClientOrganizationId() == null?
                null:
                        clientOrganizationRepository.findById(req.getProjectLevelId())//req.getProjectLevelId())
                                .orElseThrow(ClientOrganizationNotFoundException::new);

        this.produceOrganization =
                req.getSupplierId() == null?
                        null:
                        produceOrganizationRepository.findById(req.getSupplierId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new);

        this.carType =
                req.getCarType() == null?
                        carTypeRepository.findById(-1L)
                                .orElseThrow(CarTypeNotEmptyException::new):
                        carTypeRepository.findById(req.getCarType())
                                .orElseThrow(CarTypeNotFoundException::new);


        // 업데이트 된 문서들 받아오기
        ProjectAttachmentUpdatedResult resultAttachments =
                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        true //temp End 에서 저장되는 파일들은 찐 저장
                );
        // 문서 존재한다면 add 작업 처리
        if(req.getAddedTag().size()>0) {
            addUpdatedProjectAttachments(
                    req,
                    resultAttachments.getAddedAttachments(),
                    attachmentTagRepository
            );
        }

        // 삭제하는 문서 존재한다면 delete
        if(req.getDeletedAttachments().size()>0) {
            deleteProjectAttachments(resultAttachments.getDeletedAttachments());
        }

        FileUpdatedResult fileUpdatedResults = new FileUpdatedResult(
                resultAttachments
        );

        this.clientItemNumber = req.getClientItemNumber()==null ?
                null : req.getClientItemNumber();

        return fileUpdatedResults;
    }
}
