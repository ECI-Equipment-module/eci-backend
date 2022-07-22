package eci.server.CRCOModule.entity.cr;

import eci.server.CRCOModule.dto.cr.CrUpdateRequest;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.co.CoMember;
import eci.server.CRCOModule.entity.features.CrImportance;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.entity.features.CrSource;
import eci.server.CRCOModule.exception.CrImportanceNotFoundException;
import eci.server.CRCOModule.exception.CrReasonNotFoundException;
import eci.server.CRCOModule.exception.CrSourceNotFoundException;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;

import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.NewItemImage;
import eci.server.NewItemModule.entity.NewItemMember;
import eci.server.NewItemModule.exception.*;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeRequest extends EntityDate {
    @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
     //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE3")
     //@SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)
    private Long id;

    @Column
    private String crNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crReason_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrReason crReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crImportance_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrImportance crImportance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crSource_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrSource crSource;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String content;

    @Lob
    @Column(nullable = false)
    private String solution;

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
    private boolean tempsave;

    @Column(nullable = false)
    private boolean readonly;

    @Column
    private Boolean done;


    @OneToMany(
            mappedBy = "changeRequest",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<CrAttachment> attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "change_order_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChangeOrder changeOrder;

    @OneToMany(
            mappedBy = "changeRequest",
            cascade = CascadeType.ALL,//이거
            orphanRemoval = true, //없애면 안돼 동윤아...
            fetch = FetchType.LAZY
    )
    private List<CrMember> editors;


    /**
     * attachment 존재 시 생성자
     * @param name
     * @param crNumber
     * @param crReason
     * @param crImportance
     * @param crSource
     * @param content
     * @param solution
     * @param item
     * @param member
     * @param tempsave
     * @param readonly
     * @param attachments
     */
    public ChangeRequest(
            String name,
            String crNumber,
            CrReason crReason,
            CrImportance crImportance,
            CrSource crSource,
            String content,
            String solution,
            NewItem item,
            Member member,
            boolean tempsave,
            boolean readonly,
            List<CrAttachment> attachments
    ){
        this.name = name;
        this.crNumber = crNumber;

        this.crReason = crReason;
        this.crImportance = crImportance;
        this.crSource = crSource;
        this.content = content;
        this.solution = solution;
        this.newItem = item;
        this.member = member;
        this.modifier = member;

        this.tempsave = tempsave;
        this.readonly = readonly;
        this.done = false;

        this.attachments = new ArrayList<>();
        addAttachments(attachments);

    }


    /**
     * attachment 존재하지 않을 시 생성자
     * @param name
     * @param crNumber
     * @param crReason
     * @param crImportance
     * @param crSource
     * @param content
     * @param solution
     * @param item
     * @param member
     * @param tempsave
     * @param readonly
     */
    public ChangeRequest(
            String name,
            String crNumber,
            CrReason crReason,
            CrImportance crImportance,
            CrSource crSource,
            String content,
            String solution,
            NewItem item,
            Member member,
            boolean tempsave,
            boolean readonly
    ){
        this.name = name;
        this.crNumber = crNumber;

        this.crReason = crReason;
        this.crImportance = crImportance;
        this.crSource = crSource;
        this.content = content;
        this.solution = solution;
        this.newItem = item;
        this.member = member;
        this.modifier = member;

        this.tempsave = tempsave;
        this.readonly = readonly;
        this.done = false;
    }


    /**
     * 추가할 attachments
     *
     * @param added
     */
    private void addAttachments(List<CrAttachment> added) {
        added.forEach(i -> {
                    attachments.add(i);
                    i.initChangeRequest(this);
                }
        );
    }

    private void addUpdatedAttachments
            (
                    List<Long> newTag,
                    List<String> newComment,
                    List<CrAttachment> added,
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        added.stream().forEach(i -> {
                    attachments.add(i);

                    i.initChangeRequest(this);

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
     * 업데이트 돼야 할 이미지 정보 만들어줌
     *
     * @return
     */
    private CrAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<CrAttachment> addedAttachments
                = convertCrAttachmentFilesToCrAttachments(
                addedAttachmentFiles,
                save);

        List<CrAttachment> deletedAttachments
                = convertAttachmentIdsToAttachments(deletedAttachmentIds);

        addedAttachments.stream().forEach( //06-17 added 에 들어온 것은 모두 임시저장용
                i->i.setSave(save)
        );

        return new CrAttachmentUpdatedResult(
                addedAttachmentFiles, addedAttachments, deletedAttachments);
    }

    private List<CrAttachment> convertAttachmentIdsToAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(id -> convertAttachmentIdToAttachment(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<CrAttachment> convertAttachmentIdToAttachment(Long id) {
        return this.attachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<CrAttachment> convertCrAttachmentFilesToCrAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save) {
        return attachmentFiles.stream().map(attachmentFile ->
                new CrAttachment(
                        attachmentFile.getOriginalFilename(),
                        save
                )).collect(toList());
    }

    @Getter
    @AllArgsConstructor
    public static class CrAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<CrAttachment> addedAttachments;
        private List<CrAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private CrAttachmentUpdatedResult attachmentUpdatedResult;
    }

    @Getter
    @AllArgsConstructor
    public static class CrFileUpdatedResult {
        private CrAttachmentUpdatedResult attachmentUpdatedResult;
    }
    public void updateTempsaveWhenMadeRoute() {
        this.tempsave = false;
    }

    public void updateReadOnlyWhenSaved() {
        this.readonly = true;
    }

    private void deleteAttachments(List<CrAttachment> deleted) {
        // 1) save = false 인 애들 지울 땐 찐 지우기

        for (CrAttachment att : deleted){
            if(!att.isSave()){
                this.attachments.remove(att);
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
////////////////// 수정, tempEnd 작성


    public FileUpdatedResult update(
            CrUpdateRequest req,
            NewItemRepository newItemRepository,
            CrReasonRepository crReasonRepository,
            CrImportanceRepository crImportanceRepository,
            CrSourceRepository crSourceRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<CrAttachment> targetAttaches
    )

    {
        this.tempsave = true;
        this.readonly = false;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가

        this.crNumber =
                req.getCrNumber() ==null?
                        " ":
                        this.crNumber;

        this.crReason =
                req.getCrReasonId() ==null?
                        null:
                        crReasonRepository.findById(req.getCrReasonId()).orElseThrow(CrReasonNotFoundException::new);


        this.crImportance =
                req.getCrImportanceId() ==null?
                        null:
                        crImportanceRepository.findById(req.getCrImportanceId()).orElseThrow(CrImportanceNotFoundException::new);

        this.crSource =
                req.getCrSourceId() ==null?
                        null:
                        crSourceRepository.findById(req.getCrSourceId()).orElseThrow(CrSourceNotFoundException::new);

        this.name =
                req.getName() == null
                        ||
                        req.getName().isBlank()?
                        " ":req.getName();

        this.content =
                req.getContent().isBlank()?
                        " ":
                        req.getContent();
        this.solution =
                req.getSolution().isBlank()?
                        " ":
                        req.getSolution();



        //첨부파일 시작

        CrAttachmentUpdatedResult resultAttachment =

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

    // 임시저장 종료


    public FileUpdatedResult tempEnd(
            CrUpdateRequest req,
            Long crReasonId,
            NewItemRepository newItemRepository,
            CrReasonRepository crReasonRepository,
            CrImportanceRepository crImportanceRepository,
            CrSourceRepository crSourceRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,


            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<CrAttachment> targetAttaches
    )

    {

        this.tempsave = true;
        this.readonly = true;


        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가


        this.crNumber =
                String.valueOf(this.getId()* 1000000 + (int) (Math.random() * 1000));

        this.crReason = crReasonRepository.findById(crReasonId)
                .orElseThrow(CrReasonNotFoundException::new);


        this.crImportance =
                req.getCrImportanceId() ==null?
                        crImportanceRepository.findById(-1L).orElseThrow(CrImportanceNotFoundException::new):
                        crImportanceRepository.findById(req.getCrImportanceId()).orElseThrow(CrImportanceNotFoundException::new);

        this.crSource =
                req.getCrSourceId() ==null?
                        crSourceRepository.findById(-1L).orElseThrow(CrSourceNotFoundException::new):
                        crSourceRepository.findById(req.getCrSourceId()).orElseThrow(CrSourceNotFoundException::new);

        this.name =
                req.getName() == null
                        ||
                        req.getName().isBlank()?
                        " ":req.getName();

        this.content =
                req.getContent().isBlank()?
                        " ":
                        req.getContent();
        this.solution =
                req.getSolution().isBlank()?
                        " ":
                        req.getSolution();



        //첨부파일 시작

        CrAttachmentUpdatedResult resultAttachment =

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
     * CO가 COMPLETE 되면
     * CO 에 딸린 CR 의 DONE = TRUE
     */
    public void crCompletedByCo(){
        this.done = true;
    }

    /**
     * editors 등록해주는 함수
     * @param editors
     */
    public void RegisterEditors(List<CrMember> editors){
        this.editors.clear();
        this.editors.addAll(editors);
    }


    private void oldUpdatedAttachments
            (
                    //NewItemUpdateRequest req,ㄲ
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<CrAttachment> olds, // 이 아이템의 기존 old attachments 중 deleted 빼고 아이디 오름차순
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
