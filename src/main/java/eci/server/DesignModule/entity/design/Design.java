package eci.server.DesignModule.entity.design;

import eci.server.DesignModule.dto.DesignUpdateRequest;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.project.ProjectUpdateRequest;
import eci.server.ProjectModule.entity.project.*;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.*;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
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
   @GeneratedValue(strategy = GenerationType.IDENTITY)
 //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
 //   @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
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


    public Design(

            NewItem item,
            Member member,
            boolean tempsave,
            boolean readonly
    ){

        this.newItem =item;
        this.member = member;
        this.tempsave = tempsave;
        this.readonly = readonly;
    }

    public Design(

            NewItem item,

            Member member,

            Boolean tempsave,
            Boolean readonly

    ) {
        this.newItem = item;
        this.member = member;

        this.tempsave = tempsave;
        this.readonly = readonly;


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

        this.designAttachments = new ArrayList<>();
        addDesignAttachments(designAttachments);

    }


    /**
     * 추가할 attachments
     *
     */
    private void addDesignAttachments(List<DesignAttachment> added) {
        added.stream().forEach(i -> {
            designAttachments.add(i);
            i.initDesign(this);
        });
    }


    public FileUpdatedResult update(
            DesignUpdateRequest req,
            NewItemRepository itemRepository,
            MemberRepository memberRepository
    )


    {
            this.newItem=
                itemRepository.findById(req.getItemId())
                        .orElseThrow(ItemNotFoundException::new);


        DesignAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments()
                );

        if(req.getAddedTag().size()>0) {
            addUpdatedDesignAttachments(req, resultAttachment.getAddedAttachments());
        }
        if(req.getAddedTag().size()>0) {
            deleteDesignAttachments(resultAttachment.getDeletedAttachments());
        }
        FileUpdatedResult fileUpdatedResult = new FileUpdatedResult(
                resultAttachment//, updatedAddedProjectAttachmentList
        );

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 생성자 추가



        return fileUpdatedResult;
    }

    private void addUpdatedDesignAttachments(DesignUpdateRequest req, List<DesignAttachment> added) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        AttachmentTagRepository attachmentTagRepository = null;

        added.forEach(i -> {
            designAttachments.add(i);
            i.initDesign(this);

            i.setAttach_comment(req.getAddedAttachmentComment().
                    get(
                            (added.indexOf(i))
                    )
            );
            i.setTag(attachmentTagRepository
                    .findById(req.getAddedTag().get(req.getAddedAttachments().indexOf(i))).
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
    private void deleteDesignAttachments(List<DesignAttachment> deleted) {
        deleted.
                forEach(di ->
                        this.designAttachments.remove(di)
                );
    }

    /**
     * 업데이트 돼야 할 파일 정보 만들어줌
     *
     * @return
     */
    private DesignAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds
    ) {
        List<DesignAttachment> addedAttachments
                = convertDesignAttachmentFilesToDesignAttachments(addedAttachmentFiles);
        List<DesignAttachment> deletedAttachments
                = convertDesignAttachmentIdsToDesignAttachments(deletedAttachmentIds);
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

    private List<DesignAttachment> convertDesignAttachmentFilesToDesignAttachments(List<MultipartFile> attachmentFiles) {
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
    }

}
