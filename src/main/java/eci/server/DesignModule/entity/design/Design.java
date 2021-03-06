package eci.server.DesignModule.entity.design;

import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.DesignModule.dto.DesignUpdateRequest;
import eci.server.DesignModule.entity.DesignMember;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.DesignModule.exception.DesignContentNotEmptyException;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemMember;
import eci.server.NewItemModule.exception.AttachmentTagNotFoundException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Design extends EntityDate {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

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

    @OneToMany(
            mappedBy = "design",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<DesignAttachment> designAttachments;

    @OneToMany(
            mappedBy = "design",
            cascade = CascadeType.ALL,//이거
            orphanRemoval = true, //없애면 안돼 동윤아...
            fetch = FetchType.LAZY
    )
    private List<DesignMember> editors;

    /**
     * 단순 시연용
     */
    private String designContent;

    //06-17 추가
    public void setTempsave(Boolean tempsave) {
        this.tempsave = tempsave;
    }

    //06-17 추가
    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Design(

            NewItem item,
            Member member,
            boolean tempsave,
            boolean readonly,
            String designContent // 단순 시연용
    ){

        this.newItem =item;
        this.member = member;
        this.tempsave = tempsave;
        this.readonly = readonly;
        this.modifier = member;
        this.designContent = designContent; // 단순 시연용
    }

    public Design(

            NewItem item,
            Member member,
            Boolean tempsave,
            Boolean readonly

    ) {
        this.newItem = item;
        this.member = member;

        this.modifier = member;

        this.tempsave = tempsave;
        this.readonly = readonly;


    }

    /**
     * 단순 시연용
     * @param item
     * @param member
     * @param tempsave
     * @param readonly
     * @param designAttachments
     */
    public Design(
            NewItem item,
            Member member,
            Boolean tempsave,
            Boolean readonly,
            List<DesignAttachment> designAttachments,
            String designContent //단순 시연용
    ) {

        this.newItem = item;
        this.member = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.designAttachments = new ArrayList<>();
        addDesignAttachments(designAttachments);

        this.modifier = member;

        this.designContent = designContent; //단순 시연용

    }

    public Design(
            NewItem item,
            Member member,
            Boolean tempsave,
            Boolean readonly,
            List<DesignAttachment> designAttachments

    ) {

        this.newItem = item;
        this.member = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.modifier = member;

        this.designAttachments = new ArrayList<>();
        addDesignAttachments(designAttachments);

    }


    /**
     * 추가할 attachments
     *
     */
    private void addDesignAttachments(
            List<DesignAttachment> added
    ) {
        added.stream().forEach(i -> {
            designAttachments.add(i);
            i.initDesign(this);
        });
    }


    public FileUpdatedResult update(
            DesignUpdateRequest req,
            NewItemRepository itemRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<DesignAttachment> targetAttaches
    )


    {

        this.tempsave = true; //라우트 작성하기 전이니깐 !
        this.readonly = false;  //0605- 이 부분하나가 변경, 이 것은 얘를 false 에서 true로 변경 !

        this.newItem=
                itemRepository.findById(req.getItemId())
                        .orElseThrow(ItemNotFoundException::new);

//
//        DesignAttachmentUpdatedResult resultAttachment =
//
//                findAttachmentUpdatedResult(
//                        req.getAddedAttachments(),
//                        req.getDeletedAttachments()
//                );
//
//        if(req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
//            addUpdatedDesignAttachments(
//                    newTags,
//                    newComment,
//                    resultAttachment.getAddedAttachments(),
//                    attachmentTagRepository);
//        }
//        if(req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
//            deleteDesignAttachments(resultAttachment.getDeletedAttachments());
//        }
//        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
//                resultAttachment//, updatedAddedProjectAttachmentList
//        );

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가

        this.setModifiedAt(LocalDateTime.now());

        this.designContent = req.getDesignContent(); //단순 시연용


        //문서 시작

        DesignAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        false
                );

        if (req.getDeletedAttachments().size() > 0) {
            deleteDesignAttachments(
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
            addUpdatedDesignAttachments(
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
            DesignUpdateRequest req,
            NewItemRepository itemRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<DesignAttachment> targetAttaches
    )


    {
        if(req.getDesignContent().length()==0){
            throw new DesignContentNotEmptyException();
        }

        this.tempsave = true; //라우트 작성하기 전이니깐 !
        this.readonly = true; //0605- 이 부분하나가 변경, 이 것은 얘를 false 에서 true로 변경 !

        this.newItem=
                itemRepository.findById(req.getItemId())
                        .orElseThrow(ItemNotFoundException::new);


//        DesignAttachmentUpdatedResult resultAttachment =
//
//                findAttachmentUpdatedResult(
//                        req.getAddedAttachments(),
//                        req.getDeletedAttachments()
//                );
//
//        if(req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
//            addUpdatedDesignAttachments(req, resultAttachment.getAddedAttachments(), attachmentTagRepository);
//        }
//        if(req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
//            deleteDesignAttachments(resultAttachment.getDeletedAttachments());
//        }
//        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
//                resultAttachment//, updatedAddedProjectAttachmentList
//        );

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가

        this.setModifiedAt(LocalDateTime.now());

        this.designContent = req.getDesignContent(); //단순 시연용


        //문서 시작

        DesignAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        true
                );

        if (req.getDeletedAttachments().size() > 0) {
            deleteDesignAttachments(
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
            addUpdatedDesignAttachments(
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

    private void addUpdatedDesignAttachments(

            List<Long> newTag,
            List<String> newComment,
            List<DesignAttachment> added,

            AttachmentTagRepository attachmentTagRepository
    ) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.forEach(i -> {
            designAttachments.add(i);
            i.initDesign(this);

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
//    private void deleteDesignAttachments(List<DesignAttachment> deleted) {
//        deleted.
//                forEach(di ->
//                        this.designAttachments.remove(di)
//                );
//    }
    private void deleteDesignAttachments(List<DesignAttachment> deleted) {
        // 1) save = false 인 애들 지울 땐 찐 지우기

        for (DesignAttachment att : deleted){
            if(!att.isSave()){
                this.designAttachments.remove(att);
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
    private DesignAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<DesignAttachment> addedAttachments
                = convertDesignAttachmentFilesToDesignAttachments(addedAttachmentFiles);
        List<DesignAttachment> deletedAttachments
                = convertDesignAttachmentIdsToDesignAttachments(deletedAttachmentIds);
        addedAttachments.stream().forEach( //06-17 added 에 들어온 것은 모두 임시저장용
                i->i.setSave(save)
        );
        return new DesignAttachmentUpdatedResult(addedAttachmentFiles, addedAttachments, deletedAttachments);
    }


    private List<DesignAttachment> convertDesignAttachmentIdsToDesignAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(this::convertDesignAttachmentIdToDesignAttachment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<DesignAttachment> convertDesignAttachmentIdToDesignAttachment(Long id) {
        return this.designAttachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    /**
     * 어찌저찌
     * @param attachmentFiles
     * @return
     */
    private List<DesignAttachment> convertDesignAttachmentFilesToDesignAttachments
            (List<MultipartFile> attachmentFiles) {
        return attachmentFiles.stream().map(attachmentFile -> new DesignAttachment(
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
    public static class DesignAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<DesignAttachment> addedAttachments;
        private List<DesignAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private DesignAttachmentUpdatedResult designUpdatedResult;
    }


    public void finalSaveDesign(){
        //라우트까지 만들어져야 temp save 가 비로소 true
        this.tempsave = false;
        this.readonly = true;
    }

    /**
     * editors 등록해주는 함수
     * @param editors
     */
    public void RegisterEditors(List<DesignMember> editors){
        this.editors.clear();
        this.editors.addAll(editors);
    }

    private void oldUpdatedAttachments
            (
                    //NewItemUpdateRequest req,ㄲ
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<DesignAttachment> olds, // 이 아이템의 기존 old attachments 중 deleted 빼고 아이디 오름차순
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
