package eci.server.DocumentModule.entity;

import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.DocumentModule.dto.DocumentUpdateRequest;
import eci.server.DocumentModule.entity.classification.DocClassification;
import eci.server.DocumentModule.entity.classification.DocTag;
import eci.server.DocumentModule.repository.DocClassification1Repository;
import eci.server.DocumentModule.repository.DocClassification2Repository;
import eci.server.DocumentModule.repository.DocTagRepository;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItemMember;
import eci.server.NewItemModule.exception.AttachmentTagNotFoundException;
import eci.server.NewItemModule.exception.ClassificationNotFoundException;

import lombok.*;
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
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document extends EntityDate {
    @Id

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name = "SEQUENCE2", sequenceName = "SEQUENCE2", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc1_id")//, nullable = false)
    @JoinColumn(name = "doc2_id")//, nullable = false)
    private DocClassification classification;

    @Column//(nullable = false)
    private String documentTitle;

    @Lob
    @Column//(nullable = false)
    private String documentContent;

    @Column
    private String documentNumber;
    //save ??? ?????? type + id ????????? ??????
;

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
    private boolean revise_progress;

    @Column(nullable = false)
    private int revision;

    @Column
    private Integer released;

    //nullable
    @OneToOne
    @JoinColumn(name = "revise_id")
    private Document reviseTargetDoc;

    @OneToMany(
            mappedBy = "document",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<DocumentAttachment> attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_tag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DocTag docTag;

    @OneToMany(
            mappedBy = "document",
            cascade = CascadeType.ALL,//??????
            orphanRemoval = true, //????????? ?????? ?????????...
            fetch = FetchType.LAZY
    )
    private List<DocumentMember> editors;

    /**
     * tempsave ??? save ??? ?????? ?????? ?????? ?????????
     * tempsave ???
     * readonly ??????
     * ????????? doc number ?????????! ??????????????? ??????!
     * @param docClassification
     * @param title
     * @param content
     * @param member
     * @param attachments
     * @param docTag
     * @param tempsave
     * @param readonly
     */
    public Document(

            DocClassification docClassification,
            String title,
            String content,
            Member member,
            List<DocumentAttachment> attachments,
            DocTag docTag,

            String documentNumber,

            boolean tempsave,
            boolean readonly,

            List<DocumentAttachment> duplicatedAttachments
            
    ){
        this.tempsave = tempsave;
        this.readonly = readonly;

        this.classification = docClassification;
        this.documentTitle = title;
        this.documentContent = content;
        this.documentNumber = documentNumber;
        this.member = member;
        this.modifier = member;
        
        this.revision = 65;
        this.released = 0;

        if(attachments!=null) {
            this.attachments = new ArrayList<>();
            addAttachments(attachments);
        }

        this.docTag = docTag;

        if(duplicatedAttachments!=null){
            this.attachments
                    .addAll(duplicatedAttachments);
            addAttachments(duplicatedAttachments);
        }
        
    }
    
    /// ?????? ?????? ?????????

    private void addAttachments(List<DocumentAttachment> added) {
        added.forEach(i -> {
                    attachments.add(i);
                    i.initDocument(this);
                }
        );
    }

    private void addUpdatedAttachments
            (

                    List<DocumentAttachment> added

            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        added.stream().forEach(i -> {
                    attachments.add(i);

                    i.initDocument(this);

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
     * ???????????? ?????? ??? ????????? ?????? ????????????
     *
     * @return
     */
    private DocumentAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<DocumentAttachment> addedAttachments
                = convertDocumentAttachmentFilesToDocumentAttachments(
                addedAttachmentFiles,
                save);

        List<DocumentAttachment> deletedAttachments
                = convertAttachmentIdsToAttachments(deletedAttachmentIds);

        addedAttachments.stream().forEach( //06-17 added ??? ????????? ?????? ?????? ???????????????
                i -> i.setSave(save)
        );

        return new DocumentAttachmentUpdatedResult(
                addedAttachmentFiles, addedAttachments, deletedAttachments);
    }

    private List<DocumentAttachment> convertAttachmentIdsToAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(id -> convertAttachmentIdToAttachment(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<DocumentAttachment> convertAttachmentIdToAttachment(Long id) {
        return this.attachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<DocumentAttachment> convertDocumentAttachmentFilesToDocumentAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save) {
        return attachmentFiles.stream().map(attachmentFile ->
                new DocumentAttachment(
                        attachmentFile.getOriginalFilename(),
                        save
                )).collect(toList());
    }






    public FileUpdatedResult update(
            DocumentUpdateRequest req,

            MemberRepository memberRepository,
            DocTagRepository docTagRepository,
            DocClassification1Repository docClassification1Repository,
            DocClassification2Repository docClassification2Repository

    ) {
        this.tempsave = true;
        this.readonly = false;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(req.getModifierId())
                        .orElseThrow(MemberNotFoundException::new);

        this.classification =
                req.getClassification1Id()==null?null:
                new DocClassification(
                        docClassification1Repository.findById(
                                req.getClassification1Id()
                        ).orElseThrow(ClassificationNotFoundException::new),

                        docClassification2Repository.findById(
                                req.getClassification2Id()
                        ).orElseThrow(ClassificationNotFoundException::new)

                        );

        this.documentTitle = req.getTitle()
                == null ? " " : req.getTitle();


        this.documentContent = req.getContent() == null ? " " :
                req.getContent();

        this.documentNumber = "made when saved";

        this.docTag = req.getTagId()==null?null : docTagRepository.findById(
                req.getTagId()
                                ).orElseThrow
                (AttachmentTagNotFoundException::new);


        DocumentAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        false
                );

        if(
                req.getAddedAttachments()!=null
        &&
                req.getAddedAttachments().size()>0) {
            //TODO ??? ?????? ?????? ??? ?????? ?????? (?????? ????????? ?????? ????????????)
            addUpdatedAttachments(
                    resultAttachment.getAddedAttachments()
            );
        }

        if (
                req.getDeletedAttachments()!=null
            &&
                req.getDeletedAttachments().size() > 0) {

            deleteAttachments(resultAttachment.getDeletedAttachments());
        }

        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment
        );

        return fileUpdatedResult;

    }


    public FileUpdatedResult tempEnd(
            DocumentUpdateRequest req,
            MemberRepository memberRepository,
            DocTagRepository docTagRepository,
            DocClassification1Repository docClassification1Repository,
            DocClassification2Repository docClassification2Repository

    ) {
        this.tempsave = true;
        this.readonly = true;

        this.setModifiedAt(LocalDateTime.now());

        this.modifier =
                memberRepository.findById(req.getModifierId())
                        .orElseThrow(MemberNotFoundException::new);

        this.classification =
                new DocClassification(
                        docClassification1Repository.findById(
                                req.getClassification1Id()
                        ).orElseThrow(ClassificationNotFoundException::new),

                        docClassification2Repository.findById(
                                req.getClassification2Id()
                        ).orElseThrow(ClassificationNotFoundException::new)

                );

        this.documentTitle = req.getTitle()
                == null ? " " : req.getTitle();


        this.documentContent = req.getContent() == null ? " " :
                req.getContent();

        this.documentNumber = String.valueOf
                (req.getTagId() * 1000000 +
                        (int) (Math.random() * 1000));

        this.docTag = req.getTagId()==null?null : docTagRepository.findById(
                req.getTagId()
        ).orElseThrow
                (AttachmentTagNotFoundException::new);


        DocumentAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        true
                );

        if(req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
            //TODO ??? ?????? ?????? ??? ?????? ?????? (?????? ????????? ?????? ????????????)
            addUpdatedAttachments(
                    resultAttachment.getAddedAttachments()
            );
        }

        if (req.getDeletedAttachments().size() > 0) {
            deleteAttachments(resultAttachment.getDeletedAttachments());
        }

        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment
        );

        return fileUpdatedResult;

    }

    ///// ???????????? ?????? ?????? ????????????
    @Getter
    @AllArgsConstructor
    public static class DocumentAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<DocumentAttachment> addedAttachments;
        private List<DocumentAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private DocumentAttachmentUpdatedResult attachmentUpdatedResult;
    }

    @Getter
    @AllArgsConstructor
    public static class CrFileUpdatedResult {
        private DocumentAttachmentUpdatedResult attachmentUpdatedResult;
    }

    public void updateTempsaveWhenMadeRoute() {
        this.tempsave = false;
    }

    public void updateReadOnlyWhenSaved() {
        this.readonly = true;
    }

    private void deleteAttachments(List<DocumentAttachment> deleted) {
        // 1) save = false ??? ?????? ?????? ??? ??? ?????????

        for (DocumentAttachment att : deleted) {
            if (!att.isSave()) {
                this.attachments.remove(att);
                //orphanRemoval=true??? ?????? Post???
                //?????? ????????? ???????????? ?????? ????????? ??? Image ???
                // ??????????????????????????? ??????
            }
            // 2) save = true ??? ?????? ?????? ??? ????????? ?????? ??????
            else {
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }
    }

    public void finalSaveProject() {
        //??????????????? ??????????????? temp save ??? ????????? true
        this.tempsave = false;
        this.readonly = true;
    }

    /**
     * revise ??? ?????? /???????????? ??????
     * ?????? ??????????????? ???????????? target doc ??? old doc
     * @param oldDocument
     */
    public void registerOldDoc(Document oldDocument){

        this.reviseTargetDoc = oldDocument;

    }

    /**
     * ??????????????? ????????? old doc ??? revision+1???
     * @param oldReviseCnt
     */
    public void updateRevisionAndHeritageReleased(int oldReviseCnt, int releasedCnt){
        this.revision = oldReviseCnt+1;
    }

    /**
     * old doc ??? (??????????????? ??????????????? ( revise ??????
     */

    public void reviseProgressTrue(){
        this.revise_progress=true;
    }

    /**
     * ????????? complete ?????? old doc ??? revise progress ????????????
     */
    public void reviseProgressFalse(){
        this.revise_progress=false;
    }

    /**
     * editors ??????????????? ??????
     * @param editors
     */
    public void RegisterEditors(List<DocumentMember> editors){
        this.editors.clear();
        this.editors.addAll(editors);
    }

}

