package eci.server.ProjectModule.entity.project;


import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.exception.AttachmentTagNotFoundException;
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

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String projectNumber;
    //save ??? ?????? type + id ????????? ??????

    //@DateTimeFormat(pattern = "yyyy-MM-dd") -> request??? ????????? ??? ?????? ?????? ????????????
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
            name = "modifier_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member modifier;

    @Column(nullable = false)
    private Boolean tempsave;

    @Column(nullable = false)
    private Boolean readonly; //05-12??????

    @Column(nullable = false)
    private String lifecycle;

    @Column
    private String clientItemNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectType projectType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectLevel_id")
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

    //??????
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


    @OneToMany( //0717 ??????
            mappedBy = "project",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<NewItem> newItems = new ArrayList<>();

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
        this.modifier = member;

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

        this.revision = (char) 65;
        this.lifecycle = "WORKING";

        this.clientItemNumber = clientItemNumber;

        this.modifier = member;

    }


    /**
     * Project??? Attachment ???????????? ?????? ?????? ??????????????????.
     *
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

        this.newItem = item;
        this.carType = carType;


        this.revision = (char) 65;
        this.lifecycle = "WORKING";

        this.clientItemNumber = clientItemNumber;

        this.modifier = member;
    }


    /**
     * ????????? attachments
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
            AttachmentTagRepository attachmentTagRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<ProjectAttachment> targetAttaches
    ) {

        this.readonly = false;
        this.tempsave = true;

        this.setModifiedAt(LocalDateTime.now());

        this.name = req.getName().isBlank() ? this.name : req.getName();

        this.projectType =
                req.getProjectTypeId() == null ?
                        this.projectType :
                        projectTypeRepository.findById(req.getProjectTypeId())
                                .orElseThrow(ProjectTypeNotFoundException::new);

        this.protoStartPeriod =
                req.getProtoStartPeriod() == null ||
                        req.getProtoStartPeriod().isBlank() ?
                        this.protoStartPeriod :
                        LocalDate.parse(req.getProtoStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.protoOverPeriod =
                req.getProtoStartPeriod() == null ||
                        req.getProtoOverPeriod().isBlank() ?
                        this.protoOverPeriod :
                        LocalDate.parse(req.getProtoOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1StartPeriod =
                req.getP1StartPeriod() == null ||
                        req.getP1StartPeriod().isBlank() ?
                        this.p1StartPeriod :
                        LocalDate.parse(req.getP1StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1OverPeriod =
                req.getP1OverPeriod() == null ||
                        req.getP1OverPeriod().isBlank() ?
                        this.p1OverPeriod :
                        LocalDate.parse(req.getP1OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2StartPeriod =
                req.getP2StartPeriod() == null ||
                        req.getP2StartPeriod().isBlank() ?
                        this.p2StartPeriod :
                        LocalDate.parse(req.getP2StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2OverPeriod =
                req.getP2OverPeriod() == null ||
                        req.getP2OverPeriod().isBlank() ?
                        this.protoOverPeriod :
                        LocalDate.parse(req.getP2OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopStartPeriod =
                req.getSopStartPeriod() == null ||
                        req.getSopStartPeriod().isBlank() ?
                        this.sopStartPeriod :
                        LocalDate.parse(req.getSopStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopOverPeriod =
                req.getSopOverPeriod() == null ||
                        req.getSopOverPeriod().isBlank() ?
                        this.sopOverPeriod :
                        LocalDate.parse(req.getSopOverPeriod(), DateTimeFormatter.ISO_DATE);

//        this.newItem =
//                req.getItemId() == null ?
//                        this.newItem :
//                        newItemRepository.findById(req.getItemId())
//                                .orElseThrow(ItemNotFoundException::new);

        this.projectLevel =
                req.getProjectLevelId() == null ?
                        null :
                        projectLevelRepository.findById(req.getProjectLevelId())
                                .orElseThrow(ProjectLevelNotFoundException::new);


        this.clientOrganization =
                req.getClientOrganizationId() == null ?
                        null :
                        clientOrganizationRepository.findById(req.getClientOrganizationId())//req.getProjectLevelId())
                                .orElseThrow(ClientOrganizationNotFoundException::new);

        this.produceOrganization =
                req.getSupplierId() == null ?
                        null :
                        produceOrganizationRepository.findById(req.getSupplierId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new);

        this.carType =
                req.getCarTypeId() == null ?
                        null :
                        carTypeRepository.findById(req.getCarTypeId())
                                .orElseThrow(CarTypeNotFoundException::new);


        //?????? ??????

        ProjectAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        false
                );

        if (req.getDeletedAttachments().size() > 0) {
            deleteProjectAttachments(
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
            addUpdatedProjectAttachments(
                    newTags,
                    newComment,
                    resultAttachment.getAddedAttachments(),
                    attachmentTagRepository
            );
        }

        FileUpdatedResult fileUpdatedResult =
                new FileUpdatedResult(
                        resultAttachment//, updatedAddedProjectAttachmentList
                );


        this.clientItemNumber = req.getClientItemNumber() == null ?
                null : req.getClientItemNumber();

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????


        return fileUpdatedResult;
    }

    //06-17 ??????
    public void setTempsave(Boolean tempsave) {
        this.tempsave = tempsave;
    }

    //06-17 ??????
    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    private void addUpdatedProjectAttachments(
            List<Long> newTag,
            List<String> newComment,
            List<ProjectAttachment> added,
            AttachmentTagRepository attachmentTagRepository
    ) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);

            i.setAttach_comment(
                    newComment.size()==0?" ":
                    newComment.get(
                            (added.indexOf(i))
                    ).isBlank()?
                            " ":newComment.get(
                            (added.indexOf(i))
                    )
            );
            i.setTag(attachmentTagRepository
                    .findById(newTag.get(added.indexOf(i))).
                    orElseThrow(AttachmentNotFoundException::new).getName());
            i.setAttachmentaddress(
                    "src/main/prodmedia/image/" +
                            sdf1.format(now).substring(0, 10)
                            + "/"
                            + i.getUniqueName()
            );

        });


    }


    /**
     * ????????? ????????? ?????? (?????? ?????? ????????? ??????)
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
        // 1) save = false ??? ?????? ?????? ??? ??? ?????????

        for (ProjectAttachment att : deleted) {
            if (!att.isSave()) {
                this.projectAttachments.remove(att);
                //orphanRemoval=true??? ?????? Post???
                //?????? ????????? ???????????? ?????? ????????? ??? Image???
                // ??????????????????????????? ??????
            }
            // 2) save = true ??? ?????? ?????? ??? ????????? ?????? ??????
            else {
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }
    }

    /**
     * ???????????? ?????? ??? ?????? ?????? ????????????
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

        addedAttachments.stream().forEach( //06-17 added ??? ????????? ?????? ?????? ???????????????
                i -> i.setSave(save)
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
     * ????????????
     *
     * @param attachmentFiles
     * @return
     */
    private List<ProjectAttachment> convertProjectAttachmentFilesToProjectAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save
    ) {
        return attachmentFiles.stream().map(attachmentFile ->
                new ProjectAttachment(
                        attachmentFile.getOriginalFilename(),
                        save
                )).collect(toList());
    }

    /**
     * ???????????? ?????? ???????????? ????????? ????????? ???????????? ??????
     * ??? ?????? ???????????? ????????? ?????? ?????? ???????????????
     * ????????? ?????? ?????????, ????????? ?????? ?????? => ?????? ???????????? ??????
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

    public void finalSaveProject() {
        //??????????????? ??????????????? temp save ??? ????????? true
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
            AttachmentTagRepository attachmentTagRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<ProjectAttachment> targetAttaches
    ) {

        this.setModifiedAt(LocalDateTime.now());

        this.tempsave = true; //????????? ???????????? ???????????? !
        this.readonly = true; //0605- ??? ??????????????? ??????, ??? ?????? ?????? false ?????? true??? ?????? !

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????


        this.name = req.getName() == null || req.getName().isBlank() ?
                projectLevelRepository.findById(-1L).orElseThrow(
                                NameNotEmptyException::new)
                        .getName() :
                req.getName();
        this.projectNumber =
                req.getProjectLevelId() == null || req.getProjectLevelId() == 99999L ?
                        projectLevelRepository.findById(-1L).orElseThrow(ProjectLevelNotEmptyException::new)
                                .getName() :
                        ProjectCreateRequest.ProjectNumber(req.getProjectLevelId());

        this.protoStartPeriod = req.getProtoStartPeriod() == null || req.getProtoStartPeriod().isBlank() ?
                null : LocalDate.parse(req.getProtoStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.protoOverPeriod = req.getProtoOverPeriod() == null || req.getProtoOverPeriod().isBlank() ?
                null : LocalDate.parse(req.getProtoOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1StartPeriod = req.getP1StartPeriod() == null || req.getP1StartPeriod().isBlank() ?
                null : LocalDate.parse(req.getP1StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1OverPeriod = req.getP1OverPeriod() == null || req.getP1OverPeriod().isBlank() ?
                null : LocalDate.parse(req.getP1OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2StartPeriod = req.getP2StartPeriod() == null || req.getP2StartPeriod().isBlank() ?
                null : LocalDate.parse(req.getP2StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2OverPeriod = req.getP2OverPeriod() == null || req.getP2OverPeriod().isBlank() ?
                null : LocalDate.parse(req.getP2OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopStartPeriod = req.getSopStartPeriod() == null || req.getSopStartPeriod().isBlank() ?
                null : LocalDate.parse(req.getSopStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.sopOverPeriod = req.getSopOverPeriod() == null || req.getSopOverPeriod().isBlank() ?
                null : LocalDate.parse(req.getSopOverPeriod(), DateTimeFormatter.ISO_DATE);

//
//        this.newItem =
//                req.getItemId() == null ?
//                        newItemRepository.findById(-1L)
//                                .orElseThrow(ItemTypeNotEmptyException::new) :
//                        newItemRepository.findById(req.getItemId())
//                                .orElseThrow(ItemNotFoundException::new);


        this.projectType =
                req.getProjectTypeId() == null ?
                        projectTypeRepository.findById(-1L)
                                .orElseThrow(ProjectTypeNotEmptyException::new) :
                        projectTypeRepository.findById(req.getProjectTypeId())
                                .orElseThrow(ProjectTypeNotFoundException::new);


        this.projectLevel =
                req.getProjectLevelId() == null ?
                        projectLevelRepository.findById(-1L)
                                .orElseThrow(ProjectLevelNotEmptyException::new) :
                        projectLevelRepository.findById(req.getProjectLevelId())
                                .orElseThrow(ProjectLevelNotFoundException::new);

        this.clientOrganization =
                req.getClientOrganizationId() == null ?
                        null :
                        clientOrganizationRepository.findById(req.getClientOrganizationId())//req.getProjectLevelId())
                                .orElseThrow(ClientOrganizationNotFoundException::new);

        this.produceOrganization =
                req.getSupplierId() == null ?
                        null :
                        produceOrganizationRepository.findById(req.getSupplierId())
                                .orElseThrow(ProduceOrganizationNotFoundException::new);

        this.carType =
                req.getCarTypeId() == null ?
                        carTypeRepository.findById(-1L)
                                .orElseThrow(CarTypeNotEmptyException::new) :
                        carTypeRepository.findById(req.getCarTypeId())
                                .orElseThrow(CarTypeNotFoundException::new);


        // ???????????? ??? ????????? ????????????
        ProjectAttachmentUpdatedResult resultAttachments =
                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        true //temp End ?????? ???????????? ???????????? ??? ??????
                );
        // ?????? ??????????????? add ?????? ??????
        if (req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
            addUpdatedProjectAttachments(
                    newTags,
                    newComment,
                    resultAttachments.getAddedAttachments(),
                    attachmentTagRepository
            );
        }

        // ???????????? ?????? ??????????????? delete
        if (req.getDeletedAttachments().size() > 0) {
            deleteProjectAttachments(resultAttachments.getDeletedAttachments());
        }

        FileUpdatedResult fileUpdatedResults = new FileUpdatedResult(
                resultAttachments
        );

        this.clientItemNumber = req.getClientItemNumber() == null ?
                null : req.getClientItemNumber();

        return fileUpdatedResults;
    }

    public void changeItemIdOfProjectByNewMadeItem(NewItem newMadeItem) {
    System.out.println("????????? ???????????? ??????????????????????????????????????? ");
        this.newItem = newMadeItem;
    }

    public void setNewItem(NewItem newItem) {
        System.out.println("setting newItem to "+ newItem.getId());
        this.newItem = newItem;
    }

    public NewItemCreateResponse projectUpdateToReadonlyFalseTempSaveTrue() {
        this.readonly = false;
        this.tempsave = true;
        return new NewItemCreateResponse(
                this.id
        );
    }

    public NewItemCreateResponse updateNewItem(
            Long NewItemId,
            NewItemRepository newItemRepository
    ){
        this.newItem =
                newItemRepository.findById(NewItemId)
                        .orElseThrow(ItemNotFoundException::new);

        return new NewItemCreateResponse(newItem.getId());

    }


    private void oldUpdatedAttachments
            (
                    //NewItemUpdateRequest req,???
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<ProjectAttachment> olds, // ??? ???????????? ?????? old attachments ??? deleted ?????? ????????? ????????????
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


}