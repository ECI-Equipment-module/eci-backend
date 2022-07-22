package eci.server.ReleaseModule.entity;

import eci.server.CRCOModule.entity.co.ChangeOrder;import eci.server.CRCOModule.exception.CoNotFoundException;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.exception.AttachmentTagNotFoundException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ReleaseModule.dto.ReleaseUpdateRequest;
import eci.server.ReleaseModule.exception.ReleaseOrganizationNotFoundException;
import eci.server.ReleaseModule.exception.ReleaseTypeNotFoundException;
import eci.server.ReleaseModule.repository.ReleaseOrganizationReleaseRepository;
import eci.server.ReleaseModule.repository.ReleaseOrganizationRepository;
import eci.server.ReleaseModule.repository.ReleaseTypeRepository;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Releasing extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    //@SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String releaseTitle;

    @Lob
    @Column(nullable = false)
    private String releaseContent;

    @Column
    private String releaseNumber;
    //save 할 시에 type + id 값으로 지정

    @Nullable
    @OneToOne
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

    @Nullable
    @OneToOne
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

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
    private Boolean readonly; //05-12반영

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "releaseType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ReleaseType releaseType;

    @OneToMany(
            mappedBy = "release",
            cascade = CascadeType.ALL,//MERGE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<ReleaseOrgRelease> releaseOrganization;

    @OneToMany(
            mappedBy = "release",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ReleaseAttachment> attachments;

    //////////////////////////////////////////////

    /**
     * 문서 존재 생성자
     *
     * @param releaseTitle
     * @param releaseContent
     * @param releaseNumber
     * @param newItem
     * @param changeOrder
     * @param member
     * @param tempsave
     * @param readonly
     * @param releaseType
     * @param releaseOrganizations
     * @param releaseAttachments
     */
    public Releasing(

            String releaseTitle,
            String releaseContent,
            String releaseNumber,
            NewItem newItem,
            ChangeOrder changeOrder,
            Member member,

            Boolean tempsave,
            Boolean readonly,

            ReleaseType releaseType,
            List<ReleaseOrganization> releaseOrganizations,
            List<ReleaseAttachment> releaseAttachments

    ) {
        this.releaseTitle = releaseTitle;

        this.releaseContent = releaseContent;

        this.releaseNumber = releaseNumber;

        this.newItem = newItem;

        this.changeOrder = changeOrder;

        this.member = member;
        this.modifier = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.releaseType = releaseType;

        this.releaseOrganization = releaseOrganizations.stream().map(
                        //다대다 관계를 만드는 구간
                        ro -> new ReleaseOrgRelease(

                                ro,
                                this
                        )
                )
                .collect(Collectors.toSet());

        this.attachments = new ArrayList<>();
        addAttachments(releaseAttachments);

    }

    /**
     * attachment 없는
     *
     * @param releaseTitle
     * @param releaseContent
     * @param releaseNumber
     * @param newItem
     * @param changeOrder
     * @param member
     * @param tempsave
     * @param readonly
     * @param releaseType
     * @param releaseOrganizations
     */
    public Releasing(

            String releaseTitle,
            String releaseContent,
            String releaseNumber,
            NewItem newItem,
            ChangeOrder changeOrder,
            Member member,

            Boolean tempsave,
            Boolean readonly,

            ReleaseType releaseType,
            List<ReleaseOrganization> releaseOrganizations
    ) {
        this.releaseTitle = releaseTitle;

        this.releaseContent = releaseContent;

        this.releaseNumber = releaseNumber;

        this.newItem = newItem;

        this.changeOrder = changeOrder;

        this.member = member;
        this.modifier = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.releaseType = releaseType;

        this.releaseOrganization = releaseOrganizations.stream().map(
                        //다대다 관계를 만드는 구간
                        ro -> new ReleaseOrgRelease(

                                ro,
                                this
                        )
                )
                .collect(Collectors.toSet());


    }


    private void addAttachments(List<ReleaseAttachment> added) {
        added.forEach(i -> {
                    attachments.add(i);
                    i.initRelease(this);
                }
        );
    }

    private void addUpdatedAttachments
            (
                    List<Long> newTag,
                    List<String> newComment,
                    List<ReleaseAttachment> added,
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        added.stream().forEach(i -> {
                    attachments.add(i);

                    i.initRelease(this);

                    //
            i.setAttach_comment(
                    newComment.size()==0?
                            " ":
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
                                    sdf1.format(now).substring(0, 10)
                                    + "/"
                                    + i.getUniqueName()
                    );

                }
        );

    }

    /**
     * 업데이트 돼야 할 이미지 정보 만들어줌
     *
     * @return
     */
    private ReleaseAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<ReleaseAttachment> addedAttachments
                = convertReleaseAttachmentFilesToReleaseAttachments(
                addedAttachmentFiles,
                save);

        List<ReleaseAttachment> deletedAttachments
                = convertAttachmentIdsToAttachments(deletedAttachmentIds);

        addedAttachments.stream().forEach( //06-17 added 에 들어온 것은 모두 임시저장용
                i -> i.setSave(save)
        );

        return new ReleaseAttachmentUpdatedResult(
                addedAttachmentFiles, addedAttachments, deletedAttachments);
    }

    private List<ReleaseAttachment> convertAttachmentIdsToAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(id -> convertAttachmentIdToAttachment(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<ReleaseAttachment> convertAttachmentIdToAttachment(Long id) {
        return this.attachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<ReleaseAttachment> convertReleaseAttachmentFilesToReleaseAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save) {
        return attachmentFiles.stream().map(attachmentFile ->
                new ReleaseAttachment(
                        attachmentFile.getOriginalFilename(),
                        save
                )).collect(toList());
    }

    @Getter
    @AllArgsConstructor
    public static class ReleaseAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<ReleaseAttachment> addedAttachments;
        private List<ReleaseAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private ReleaseAttachmentUpdatedResult attachmentUpdatedResult;
    }

    @Getter
    @AllArgsConstructor
    public static class CrFileUpdatedResult {
        private ReleaseAttachmentUpdatedResult attachmentUpdatedResult;
    }

    public void updateTempsaveWhenMadeRoute() {
        this.tempsave = false;
    }

    public void updateReadOnlyWhenSaved() {
        this.readonly = true;
    }

    private void deleteAttachments(List<ReleaseAttachment> deleted) {
        // 1) save = false 인 애들 지울 땐 찐 지우기

        for (ReleaseAttachment att : deleted) {
            if (!att.isSave()) {
                this.attachments.remove(att);
                //orphanRemoval=true에 의해 Post와
                //연관 관계가 끊어지며 고아 객체가 된 Image는
                // 데이터베이스에서도 제거
            }
            // 2) save = true 인 애들 지울 땐 아래와 같이 진행
            else {
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }
    }

    public void finalSaveRelease() {
        //라우트까지 만들어져야 temp save 가 비로소 true
        this.tempsave = false;
        this.readonly = true;
    }

    /**
     * update
     */
    public FileUpdatedResult update(
            ReleaseUpdateRequest req,

            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,
            NewItemRepository newItemRepository,
            ChangeOrderRepository changeOrderRepository,
            ReleaseOrganizationRepository releaseOrganizationRepository,
            ReleaseTypeRepository releaseTypeRepository,
            ReleaseOrganizationReleaseRepository releaseOrganizationReleaseRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<ReleaseAttachment> targetAttaches

    ) {

        this.tempsave = true;
        this.readonly = false;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(req.getModifierId())
                        .orElseThrow(MemberNotFoundException::new);

        this.releaseTitle = req.getReleaseTitle()
                == null ? " " : req.getReleaseTitle();

        this.releaseContent = req.getReleaseContent() == null ? " " :
                req.getReleaseContent();

//        this.releaseNumber = req.getReleaseNumber() == null ? " " :
//                req.getReleaseNumber();

        this.newItem = req.getReleaseItemId() == null ?
                null :
                newItemRepository.findById(req.getReleaseItemId())
                        .orElseThrow(ItemNotFoundException::new);

        this.changeOrder = req.getReleaseCoId() == null ?
                null :
                changeOrderRepository.findById(req.getReleaseCoId()).
                        orElseThrow(CoNotFoundException::new);

        this.tempsave = true;
        this.readonly = false;

        this.releaseType = req.getReleaseType() == null ?
                null:
                releaseTypeRepository.findById(
                        req.getReleaseType()
                ).orElseThrow(ReleaseTypeNotFoundException::new);

        this.releaseOrganization.clear();

        Set<ReleaseOrgRelease> releaseOrganizations =
                req.getReleaseOrganizationId().size() == 0 ?
                        null :
                        (req.getReleaseOrganizationId().stream().map(
                                i -> releaseOrganizationRepository.findById(i)
                                        .orElseThrow(ReleaseOrganizationNotFoundException::new)
                        ).collect(toList())) //입력으로 받아온 co effect 값
                                .stream().map(
                                        //다대다 관계를 만드는 구간
                                        ro ->
                                                new ReleaseOrgRelease(
                                                        ro,
                                                        this
                                                )

                                ).collect(Collectors.toSet());

        this.releaseOrganization.addAll(releaseOrganizations);
//
//        ReleaseAttachmentUpdatedResult resultAttachment =
//
//                findAttachmentUpdatedResult(
//                        req.getAddedAttachments(),
//                        req.getDeletedAttachments(),
//                        false
//                );
//
//        if (req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
//            addUpdatedAttachments(
//                    newTags,
//                    newComment,
//                    resultAttachment.getAddedAttachments(),
//                    attachmentTagRepository
//            );
//            //addProjectAttachments(resultAttachment.getAddedAttachments());
//        }
//
//        if (req.getDeletedAttachments().size() > 0) {
//            deleteAttachments(resultAttachment.getDeletedAttachments());
//        }
//
//        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
//                resultAttachment
//        );


        //문서 시작

        ReleaseAttachmentUpdatedResult resultAttachment =

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

        FileUpdatedResult fileUpdatedResult =
                new FileUpdatedResult(
                        resultAttachment//, updatedAddedProjectAttachmentList
                );

        return fileUpdatedResult;

    }

    public FileUpdatedResult tempEnd(
            ReleaseUpdateRequest req,
            String releaseNumber,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,
            NewItemRepository newItemRepository,
            ChangeOrderRepository changeOrderRepository,
            ReleaseOrganizationRepository releaseOrganizationRepository,
            ReleaseTypeRepository releaseTypeRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<ReleaseAttachment> targetAttaches

    ) {



        this.tempsave = true;
        this.readonly = true;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);

//
//        ReleaseAttachmentUpdatedResult resultAttachment =
//
//                findAttachmentUpdatedResult(
//                        req.getAddedAttachments(),
//                        req.getDeletedAttachments(),
//                        true
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

        this.releaseNumber = releaseNumber;


        this.modifier =
                memberRepository.findById(req.getModifierId())
                        .orElseThrow(MemberNotFoundException::new);

        this.releaseTitle = req.getReleaseTitle()
                == null ? " " : req.getReleaseTitle();

        this.releaseContent = req.getReleaseContent() == null ? " " :
                req.getReleaseContent();

//        this.releaseNumber = req.getReleaseNumber() == null ? " " :
//                req.getReleaseNumber();

        this.newItem = req.getReleaseItemId() == null ?
                null :
                newItemRepository.findById(req.getReleaseItemId())
                        .orElseThrow(ItemNotFoundException::new);

        this.changeOrder = req.getReleaseCoId() == null ?
                null :
                changeOrderRepository.findById(req.getReleaseCoId()).
                        orElseThrow(CoNotFoundException::new);

        this.tempsave = true;
        this.readonly = false;

        this.releaseType = req.getReleaseType() == null ?
                null:
                releaseTypeRepository.findById(
                        req.getReleaseType()
                ).orElseThrow(ReleaseTypeNotFoundException::new);

        this.releaseOrganization.clear();

        Set<ReleaseOrgRelease> releaseOrganizations =
                req.getReleaseOrganizationId().size() == 0 ?
                        null :
                        (req.getReleaseOrganizationId().stream().map(
                                i -> releaseOrganizationRepository.findById(i).orElseThrow(ReleaseOrganizationNotFoundException::new)
                        ).collect(toList())) //입력으로 받아온 co effect 값
                                .stream().map(
                                        //다대다 관계를 만드는 구간
                                        ro ->
                                                new ReleaseOrgRelease(
                                                        ro,
                                                        this
                                                )

                                ).collect(Collectors.toSet());

        assert releaseOrganizations != null;
        this.releaseOrganization.addAll(releaseOrganizations);



        //문서 시작

        ReleaseAttachmentUpdatedResult resultAttachment =

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

        FileUpdatedResult fileUpdatedResult =
                new FileUpdatedResult(
                        resultAttachment//, updatedAddedProjectAttachmentList
                );

        return fileUpdatedResult;

    }


    private void oldUpdatedAttachments
            (
                    //NewItemUpdateRequest req,ㄲ
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<ReleaseAttachment> olds, // 이 아이템의 기존 old attachments 중 deleted 빼고 아이디 오름차순
                    AttachmentTagRepository attachmentTagRepository
            ) {

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