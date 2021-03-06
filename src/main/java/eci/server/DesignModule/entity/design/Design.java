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
    private Boolean readonly; //05-12??????

    @OneToMany(
            mappedBy = "design",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<DesignAttachment> designAttachments;

    @OneToMany(
            mappedBy = "design",
            cascade = CascadeType.ALL,//??????
            orphanRemoval = true, //????????? ?????? ?????????...
            fetch = FetchType.LAZY
    )
    private List<DesignMember> editors;

    /**
     * ?????? ?????????
     */
    private String designContent;

    //06-17 ??????
    public void setTempsave(Boolean tempsave) {
        this.tempsave = tempsave;
    }

    //06-17 ??????
    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Design(

            NewItem item,
            Member member,
            boolean tempsave,
            boolean readonly,
            String designContent // ?????? ?????????
    ){

        this.newItem =item;
        this.member = member;
        this.tempsave = tempsave;
        this.readonly = readonly;
        this.modifier = member;
        this.designContent = designContent; // ?????? ?????????
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
     * ?????? ?????????
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
            String designContent //?????? ?????????
    ) {

        this.newItem = item;
        this.member = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.designAttachments = new ArrayList<>();
        addDesignAttachments(designAttachments);

        this.modifier = member;

        this.designContent = designContent; //?????? ?????????

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
     * ????????? attachments
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

        this.tempsave = true; //????????? ???????????? ???????????? !
        this.readonly = false;  //0605- ??? ??????????????? ??????, ??? ?????? ?????? false ?????? true??? ?????? !

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
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????

        this.setModifiedAt(LocalDateTime.now());

        this.designContent = req.getDesignContent(); //?????? ?????????


        //?????? ??????

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

        this.tempsave = true; //????????? ???????????? ???????????? !
        this.readonly = true; //0605- ??? ??????????????? ??????, ??? ?????? ?????? false ?????? true??? ?????? !

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
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????

        this.setModifiedAt(LocalDateTime.now());

        this.designContent = req.getDesignContent(); //?????? ?????????


        //?????? ??????

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
     * ????????? ????????? ?????? (?????? ?????? ????????? ??????)
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
        // 1) save = false ??? ?????? ?????? ??? ??? ?????????

        for (DesignAttachment att : deleted){
            if(!att.isSave()){
                this.designAttachments.remove(att);
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
    /**
     * ???????????? ?????? ??? ?????? ?????? ????????????
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
        addedAttachments.stream().forEach( //06-17 added ??? ????????? ?????? ?????? ???????????????
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
     * ????????????
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
     * ???????????? ?????? ???????????? ????????? ????????? ???????????? ??????
     * ??? ?????? ???????????? ????????? ?????? ?????? ???????????????
     * ????????? ?????? ?????????, ????????? ?????? ?????? => ?????? ???????????? ??????
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
        //??????????????? ??????????????? temp save ??? ????????? true
        this.tempsave = false;
        this.readonly = true;
    }

    /**
     * editors ??????????????? ??????
     * @param editors
     */
    public void RegisterEditors(List<DesignMember> editors){
        this.editors.clear();
        this.editors.addAll(editors);
    }

    private void oldUpdatedAttachments
            (
                    //NewItemUpdateRequest req,???
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<DesignAttachment> olds, // ??? ???????????? ?????? old attachments ??? deleted ?????? ????????? ????????????
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
