package eci.server.CRCOModule.entity.co;

import eci.server.CRCOModule.dto.co.CoUpdateRequest;
import eci.server.CRCOModule.entity.CoCoEffect;
import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.CRCOModule.entity.cofeatures.ChangedFeature;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import eci.server.CRCOModule.entity.cofeatures.CoStage;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.entity.features.CrImportance;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.exception.*;
import eci.server.CRCOModule.repository.co.CoCoEffectRepository;
import eci.server.CRCOModule.repository.cofeature.ChangedFeatureRepository;
import eci.server.CRCOModule.repository.cofeature.CoEffectRepository;
import eci.server.CRCOModule.repository.cofeature.CoStageRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.exception.AttachmentTagNotFoundException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.entity.project.CarType;
import eci.server.ProjectModule.entity.project.ClientOrganization;
import eci.server.ProjectModule.exception.CarTypeNotEmptyException;
import eci.server.ProjectModule.exception.ClientOrganizationNotFoundException;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeOrder extends EntityDate {
    @Id
      //@GeneratedValue(strategy = GenerationType.IDENTITY)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE3")
     @SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    @Column
    private String clientItemNumber;

    @Column(nullable = false)
    private String coNumber;

    @Column
    private LocalDate coPublishPeriod;

    @Column
    private LocalDate coReceivedPeriod;

    @Column
    private Boolean difference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;

    @Column
    private Boolean costDifferent;

    @Column
    private String costDifference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_reason_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrReason coReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_stage_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoStage coStage;

    @Column
    private LocalDate applyPeriod;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "co_effect_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private CoEffect coEffect;
    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    //affected item
    private Set<CoCoEffect> coEffect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_importance_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrImportance coImportance;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String content;

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

//    @OneToMany(
//            mappedBy = "changeOrder",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY)
    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ChangeRequest> changeRequests = new ArrayList<>();

    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    //affected item
    private List<CoNewItem> coNewItems;

    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<CoAttachment> attachments;

    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.ALL,//??????
            orphanRemoval = true, //????????? ?????? ?????????...
            fetch = FetchType.LAZY
    )
    private List<CoMember> editors;

    ////////////////////////////////////////////////////

    /**
     * attachment ?????? ??? ?????????
     */
    public ChangeOrder(
            ClientOrganization clientOrganization,
            String clientItemNumber,
            String coNumber,
            LocalDate coPublishPeriod,
            LocalDate coReceivedPeriod,
            Boolean difference,
            CarType carType,
            Boolean costDifferent,
            String costDifference,
            CrReason coReason,
            CoStage coStage,
            LocalDate applyPeriod,

            //CoEffect coEffect,
            List<CoEffect> coEffect,

            CrImportance coImportance,
            String name,
            String content,
            Member member,
            boolean tempsave,
            boolean readonly,

            List<ChangeRequest> changeRequests,
            List<ChangedFeature> changedFeatures,
            List<String> changedContents,

            List<NewItem> newItems,
            List<CoAttachment> attachments


    ){

        this.clientOrganization = clientOrganization;
        this.clientItemNumber = clientItemNumber;
        this.coNumber = coNumber;
        this.coPublishPeriod = coPublishPeriod;
        this.coReceivedPeriod = coReceivedPeriod;
        this.difference = difference;
        this.carType = carType;
        this.costDifferent = costDifferent;
        this.costDifference = costDifference;
        this.coReason = coReason;
        this.coStage = coStage;
        this.applyPeriod = applyPeriod;

        this.coEffect = coEffect.stream().map(
                        //????????? ????????? ????????? ??????
                        effect -> new CoCoEffect(
//                                Long.parseLong((this.getId().toString()+
//                                        effect.getId().toString())),
                                this,
                                effect
                        )
                )
                .collect(Collectors.toSet());

        this.coImportance = coImportance;
        this.name = name;
        this.content = content;
        this.member = member;
        this.modifier = member;
        this.tempsave = tempsave;
        this.readonly = readonly;

        for(ChangeRequest cr : changeRequests) {
            cr.setChangeOrder(this);
        }

        this.coNewItems = newItems.stream().map(

                                //????????? ????????? ????????? ??????
                                item -> new CoNewItem(
                                        this,
                                        item,

                                        changedFeatures.size()==0||
                                        changedFeatures.get(newItems.indexOf(item))==null?
                                                null //temp ????????? ??? ??????
                                                :changedFeatures.get(newItems.indexOf(item)),

                                        changedContents.size()==0||
                                        changedContents.get(newItems.indexOf(item)).isBlank()?
                                                ""
                                                :changedContents.get(newItems.indexOf(item))
                                )
                        )
                        .collect(toList());


        this.attachments = new ArrayList<>();

        addAttachments(attachments);

    }


    /**
     * attachment ???????????? ?????? ??? ?????????
     */
    public ChangeOrder(
            ClientOrganization clientOrganization,
            String clientItemNumber,
            String coNumber,
            LocalDate coPublishPeriod,
            LocalDate coReceivedPeriod,
            Boolean difference,
            CarType carType,
            Boolean costDifferent,
            String costDifference,
            CrReason coReason,
            CoStage coStage,
            LocalDate applyPeriod,

            List<CoEffect> coEffect,

            CrImportance coImportance,
            String name,
            String content,
            Member member,
            boolean tempsave,
            boolean readonly,

            List<ChangeRequest> changeRequests,
            List<ChangedFeature> changedFeatures,
            List<String> changedContents,

            List<NewItem> newItems
    ){

        this.clientOrganization = clientOrganization;
        this.clientItemNumber = clientItemNumber;
        this.coNumber = coNumber;
        this.coPublishPeriod = coPublishPeriod;
        this.coReceivedPeriod = coReceivedPeriod;
        this.difference = difference;
        this.carType = carType;
        this.costDifferent = costDifferent;
        this.costDifference = costDifference;
        this.coReason = coReason;
        this.coStage = coStage;
        this.applyPeriod = applyPeriod;

        this.coEffect = coEffect.stream().map(
                        //????????? ????????? ????????? ??????
                        effect -> new CoCoEffect(
                                this,
                                effect
                        )
                )
                .collect(Collectors.toSet());

        this.coImportance = coImportance;
        this.name = name;
        this.content = content;
        this.member = member;
        this.modifier = member;
        this.tempsave = tempsave;
        this.readonly = readonly;

        for(ChangeRequest cr : changeRequests) {
            cr.setChangeOrder(this);
        }

        this.coNewItems = newItems.stream().map(

                        //????????? ????????? ????????? ??????
                        item -> new CoNewItem(
                                this,
                                item,

                                changedFeatures.size()==0||
                                changedFeatures.get(newItems.indexOf(item))==null?
                                        null //temp ????????? ??? ??????
                                        :changedFeatures.get(newItems.indexOf(item)),

                                changedContents.size()==0||
                                changedContents.get(newItems.indexOf(item)).isBlank()?
                                        "":changedContents.get(newItems.indexOf(item))
                        )
                )
                .collect(toList());

    }

    /**
     * ????????? attachments
     *
     * @param added
     */
    private void addAttachments(List<CoAttachment> added) {
        added.forEach(i -> {
                    attachments.add(i);
                    i.initChangeOrder(this);
                }
        );
    }

    private void addUpdatedAttachments
            (
                    List<Long> newTag,
                    List<String> newComment,
                    List<CoAttachment> added,
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        added.stream().forEach(i -> {
                    attachments.add(i);

                    i.initChangeOrder(this);

                    //
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
     * ???????????? ?????? ??? ????????? ?????? ????????????
     *
     * @return
     */
    private CoAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<CoAttachment> addedAttachments
                = convertCoAttachmentFilesToCoAttachments(
                addedAttachmentFiles,
                save);

        List<CoAttachment> deletedAttachments
                = convertAttachmentIdsToAttachments(deletedAttachmentIds);

        addedAttachments.stream().forEach( //06-17 added ??? ????????? ?????? ?????? ???????????????
                i->i.setSave(save)
        );

        return new CoAttachmentUpdatedResult(
                addedAttachmentFiles, addedAttachments, deletedAttachments);
    }

    private List<CoAttachment> convertAttachmentIdsToAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(id -> convertAttachmentIdToAttachment(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<CoAttachment> convertAttachmentIdToAttachment(Long id) {
        return this.attachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<CoAttachment> convertCoAttachmentFilesToCoAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save) {
        return attachmentFiles.stream().map(attachmentFile ->
                new CoAttachment(
                        attachmentFile.getOriginalFilename(),
                        save
                )).collect(toList());
    }

    @Getter
    @AllArgsConstructor
    public static class CoAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<CoAttachment> addedAttachments;
        private List<CoAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private CoAttachmentUpdatedResult attachmentUpdatedResult;
    }

    @Getter
    @AllArgsConstructor
    public static class CrFileUpdatedResult {
        private CoAttachmentUpdatedResult attachmentUpdatedResult;
    }
    public void updateTempsaveWhenMadeRoute() {
        this.tempsave = false;
    }

    public void updateReadOnlyWhenSaved() {
        this.readonly = true;
    }

    private void deleteAttachments(List<CoAttachment> deleted) {
        // 1) save = false ??? ?????? ?????? ??? ??? ?????????

        for (CoAttachment att : deleted){
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

    /////////////temp save, update


    public FileUpdatedResult update(
            CoUpdateRequest req,
            Long CoReasonId,

            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository,
            CrReasonRepository coReasonRepository,
            CoEffectRepository coEffectRepository,
            CoStageRepository coStageRepository,
            MemberRepository memberRepository,
            CrImportanceRepository coImportanceRepository,
            AttachmentTagRepository attachmentTagRepository,
            NewItemRepository newItemRepository, // coNewItem?????? ????????????

            ChangeRequestRepository changeRequestRepository,

            ChangedFeatureRepository changedFeatureRepository,
            CoCoEffectRepository coCoEffectRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<CoAttachment> targetAttaches
    )

    {

        this.tempsave = true;
        this.readonly = false;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(req.getModifierId())
                        .orElseThrow(MemberNotFoundException::new);

        this.clientOrganization = req.getClientOrganizationId()==null?
                null:clientOrganizationRepository.findById(
                        req.getClientOrganizationId())
                .orElseThrow(ClientOrganizationNotFoundException::new);

        this.clientItemNumber = req.getClientItemNumber()==null||req.getClientItemNumber().isBlank()?
                " ":req.getClientItemNumber();

        this.coNumber =
                this.coNumber =
                        String.valueOf(this.id * 1000000 + (int) (Math.random() * 1000));

        this.coPublishPeriod =
                req.getCoPublishPeriod() ==null||
                        req.getCoPublishPeriod().isEmpty()?
                        null:
                        LocalDate.parse
                                (req.getCoPublishPeriod(), DateTimeFormatter.ISO_DATE);

        this.coReceivedPeriod =
                req.getCoReceivedPeriod() ==null||
                        req.getCoReceivedPeriod().isEmpty()
                        ?
                        null:
                        LocalDate.parse
                                (req.getCoReceivedPeriod(), DateTimeFormatter.ISO_DATE);

        this.difference =
                req.getDifference() != null && req.getDifference();

        this.carType =
                req.getCarTypeId()==null?
                        null:
                        carTypeRepository.findById(req.getCarTypeId())
                                .orElseThrow(CarTypeNotEmptyException::new);

        this.costDifferent =
                req.getCostDifferent() != null && req.getCostDifferent();

        this.costDifference =
                req.getCostDifference() == null
                        ||
                        req.getCostDifference().isBlank()?
                        " ":req.getCostDifference();

        this.coReason =
                coReasonRepository.findById(CoReasonId).
        orElseThrow(CrReasonNotFoundException::new);

        this.coStage =
                req.getCoStageId()==null?
                        null:
                        coStageRepository.findById(req.getCoStageId()).
        orElseThrow(CoStageNotFoundException::new);

        this.applyPeriod =
                        req.getApplyPeriod() ==null||
                                req.getApplyPeriod().isEmpty()?
                                null:
                                LocalDate.parse
                                        (req.getApplyPeriod(), DateTimeFormatter.ISO_DATE);


        this.coEffect.clear();

        Set<CoCoEffect> coCoEffectsList =
                req.getCoEffectId()==null || req.getCoEffectId().size()==0?
                        null
                        :(req.getCoEffectId().stream().map(
                        i -> coEffectRepository.findById(i)
                                .orElseThrow(CrEffectNotFoundException::new)
                ).collect(toList())) //???????????? ????????? co effect ???
                        .stream().map(
                                //????????? ????????? ????????? ??????
                                effect -> new CoCoEffect(
                                        this,
                                        effect
                                )
                        ).collect(Collectors.toSet());

        this.coEffect.addAll(coCoEffectsList);

        this.coImportance = req.getCoImportanceId()==null?
                null:
                coImportanceRepository.findById(req.getCoImportanceId()).
                        orElseThrow(CrImportanceNotFoundException::new);

        this.name = req.getName().isEmpty()||req.getName()==null?
                " ":req.getName();

        this.content = req.getContent().isEmpty()||req.getContent()==null?
                " ":req.getContent();


        if(this.changeRequests!=null) {
            this.changeRequests.clear();
        }

        if(req.getChangeRequestIds()!=null || req.getChangeRequestIds().size()!=0) {
            this.changeRequests.addAll(
                    (req.getChangeRequestIds().stream().map(
                            i -> changeRequestRepository.findById(i)
                                    .orElseThrow(CrNotFoundException::new)
                    ).collect(Collectors.toSet()))
            );
        }

        List<ChangedFeature> changedFeatures =
                req.getChangeFeaturesIds().stream().map(
                        i ->
                                changedFeatureRepository
                                        .findById(i)
                                        .orElseThrow(ChangeFeatureNotFoundException::new)
                ).collect(
                        toList()
                );

        List<NewItem> newItems = req.getNewItemsIds().stream().map(
                i ->
                        newItemRepository.findById(i).orElseThrow(ItemNotFoundException::new)
        ).collect(
                toList()
        );

        this.coNewItems.clear();
        this.coNewItems.addAll(
                newItems
                        .stream().map(

                                //????????? ????????? ????????? ??????
                                item -> new CoNewItem(
                                        this,
                                        item,

                                        changedFeatures.size()==0 ||
                                        changedFeatures
                                                .get(
                                                        req.getNewItemsIds().stream().map(
                                                                        i ->
                                                                                newItemRepository.findById(i).orElseThrow(ItemNotFoundException::new)
                                                                ).collect(
                                                                        toList()
                                                                )
                                                                .indexOf(item))==null?null //temp ????????? ??? ??????

                                                :changedFeatures.get(newItems.indexOf(item)),

                                        req.getChangeContents().get(newItems.indexOf(item)).isBlank()?
                                                " ":req.getChangeContents().get(newItems.indexOf(item))
                                )
                        )
                        .collect(toList())
                );

        //newItems


        // ?????? ??????


//        CoAttachmentUpdatedResult resultAttachment =
//
//                findAttachmentUpdatedResult(
//                        req.getAddedAttachments(),
//                        req.getDeletedAttachments(),
//                        false
//                );
//
//        if(req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
//            addUpdatedAttachments(
//                    newTags,
//                    newComment,
//                    resultAttachment.getAddedAttachments(),
//                    attachmentTagRepository
//            );
//            //addProjectAttachments(resultAttachment.getAddedAttachments());
//        }
//
//        if(req.getDeletedAttachments().size()>0) {
//            deleteAttachments(resultAttachment.getDeletedAttachments());
//        }
//
//        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
//                resultAttachment//, updatedAddedProjectAttachmentList
//        );
//
//        return fileUpdatedResult;

        CoAttachmentUpdatedResult resultAttachment =

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
                    resultAttachment.getAddedAttachments(),
                    attachmentTagRepository
            );
        }

        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment//, updatedAddedProjectAttachmentList
        );

        return fileUpdatedResult;
    }

    // ???????????? ??????


    public FileUpdatedResult tempEnd(
            CoUpdateRequest req,
            Long CoReasonId,

            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository,
            CrReasonRepository coReasonRepository,
            CoEffectRepository coEffectRepository,
            CoStageRepository coStageRepository,
            MemberRepository memberRepository,
            CrImportanceRepository coImportanceRepository,
            AttachmentTagRepository attachmentTagRepository,

            NewItemRepository newItemRepository, // coNewItem?????? ????????????

            ChangeRequestRepository changeRequestRepository,

            ChangedFeatureRepository changedFeatureRepository,

            CoCoEffectRepository coCoEffectRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<CoAttachment> targetAttaches
    )

    {
        this.tempsave = true;
        this.readonly = true;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);

        this.clientOrganization = req.getClientOrganizationId()==null?

                clientOrganizationRepository.findById(-1L)
                        .orElseThrow(ClientOrganizationNotFoundException::new):
                clientOrganizationRepository.findById(
                        req.getClientOrganizationId())
                .orElseThrow(ClientOrganizationNotFoundException::new);


        this.coNumber =
                        String.valueOf(this.id * 1000000 + (int) (Math.random() * 1000));

        this.coPublishPeriod =
                req.getCoPublishPeriod() ==null||
                        req.getCoPublishPeriod().isEmpty()?
                        null:
                        LocalDate.parse
                                (req.getCoPublishPeriod(), DateTimeFormatter.ISO_DATE);

        this.coReceivedPeriod =
                req.getCoReceivedPeriod() ==null||
                        req.getCoReceivedPeriod().isEmpty()
                        ?
                        null:
                        LocalDate.parse
                                (req.getCoReceivedPeriod(), DateTimeFormatter.ISO_DATE);

        this.difference =
                req.getDifference() != null && req.getDifference();

        this.carType =
                req.getCarTypeId()==null?
                        carTypeRepository.findById(-1L).orElseThrow(CarTypeNotEmptyException::new)
    :
                        carTypeRepository.findById(req.getCarTypeId())
                                .orElseThrow(CarTypeNotEmptyException::new);

        this.costDifferent =
                req.getCostDifferent() != null && req.getCostDifferent();

        this.costDifference =
                req.getCostDifference() == null
                        ||
                        req.getCostDifference().isBlank()?
                        " ":req.getCostDifference();

        this.coReason =
                coReasonRepository.findById(CoReasonId).
                        orElseThrow(CrReasonNotFoundException::new);

        this.coStage =
                req.getCoStageId()==null?
                        coStageRepository.findById(-1L).
                                orElseThrow(CoStageNotFoundException::new)
    :
                        coStageRepository.findById(req.getCoStageId()).
                                orElseThrow(CoStageNotFoundException::new);

        this.applyPeriod =
                req.getApplyPeriod() ==null||
                        req.getApplyPeriod().isEmpty()?
                        null:
                        LocalDate.parse
                                (req.getApplyPeriod(), DateTimeFormatter.ISO_DATE);

        this.coEffect.clear();

        Set<CoCoEffect> coCoEffectsList =
                req.getCoEffectId()==null || req.getCoEffectId().size()==0?
                        null
                        :(req.getCoEffectId().stream().map(
                        i -> coEffectRepository.findById(i).orElseThrow(CrEffectNotFoundException::new)
                ).collect(toList())) //???????????? ????????? co effect ???
                        .stream().map(
                                //????????? ????????? ????????? ??????
                                effect -> new CoCoEffect(
                                        this,
                                        effect
                                )
                        ).collect(Collectors.toSet());

        this.coEffect.addAll(coCoEffectsList);

        this.coImportance = req.getCoImportanceId()==null?
                coImportanceRepository.findById(-1L).
                        orElseThrow(CrImportanceNotFoundException::new)
    :
                coImportanceRepository.findById(req.getCoImportanceId()).
                        orElseThrow(CrImportanceNotFoundException::new);

        this.name = req.getName().isEmpty()||req.getName()==null?
                " ":req.getName();

        this.content = req.getContent().isEmpty()||req.getContent()==null?
                " ":req.getContent();

        if(this.changeRequests!=null) {
            this.changeRequests.clear();
        }

        if(req.getChangeRequestIds()!=null || req.getChangeRequestIds().size()!=0) {
            this.changeRequests.addAll(
                    (req.getChangeRequestIds().stream().map(
                            i -> changeRequestRepository.findById(i)
                                    .orElseThrow(CrNotFoundException::new)
                    ).collect(Collectors.toSet()))
            );
        }
        this.changeRequests.clear();
        this.changeRequests.addAll(

                req.getChangeRequestIds().stream().map(
                        i ->
                                changeRequestRepository.findById(i)
                                        .orElseThrow(CrNotFoundException::new)
                ).collect(
                        toList()
                )
        ); //changeRequests,

        List<ChangedFeature> changedFeatures =
                req.getChangeFeaturesIds().stream().map(
                        i ->
                                changedFeatureRepository.findById(i).
                                        orElseThrow(ChangeFeatureNotFoundException::new)
                ).collect(
                        toList()
                );

        //coItem ??? ?????????
        List<NewItem> newItems = req.getNewItemsIds().stream().map(
                i ->
                        newItemRepository.findById(i).orElseThrow(ItemNotFoundException::new)
        ).collect(
                toList()
        );
        this.coNewItems.clear();
        this.coNewItems.addAll(
                newItems
                        .stream().map(

                                //????????? ????????? ????????? ??????
                                item -> new CoNewItem(
                                        this,
                                        item,
                                        changedFeatures.size()==0||
                                        changedFeatures
                                                .get(
                                                        req.getNewItemsIds().stream().map(
                                                                        i ->
                                                                                newItemRepository.findById(i).orElseThrow(ItemNotFoundException::new)
                                                                ).collect(
                                                                        toList()
                                                                )
                                                                .indexOf(item))==null?null //temp ????????? ??? ??????

                                                :changedFeatures.get(newItems.indexOf(item)),

                                        req.getChangeContents().get(newItems.indexOf(item)).isBlank()?
                                                " ":req.getChangeContents().get(newItems.indexOf(item))
                                )
                        )
                        .collect(toList())
        );

        //?????? ??????

        CoAttachmentUpdatedResult resultAttachment =

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
                    resultAttachment.getAddedAttachments(),
                    attachmentTagRepository
            );
        }

        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment//, updatedAddedProjectAttachmentList
        );

        return fileUpdatedResult;
    }

    /**
     * editors ??????????????? ??????
     * @param editors
     */
    public void RegisterEditors(List<CoMember> editors){
        this.editors.clear();
        this.editors.addAll(editors);
    }


    private void oldUpdatedAttachments
            (
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<CoAttachment> olds, // ??? ???????????? ?????? old attachments ??? deleted ?????? ????????? ????????????
                    AttachmentTagRepository attachmentTagRepository
            ) {

        // ????????? ?????? ?????? init ???????????? ???
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        olds.stream().forEach(i -> {

            i.setAttach_comment(
                    oldComment.size()==0?" ":
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
