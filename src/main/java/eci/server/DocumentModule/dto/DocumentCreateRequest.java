package eci.server.DocumentModule.dto;

import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.DocumentModule.entity.Document;
import eci.server.DocumentModule.entity.DocumentAttachment;
import eci.server.DocumentModule.entity.classification.DocClassification;
import eci.server.DocumentModule.repository.DocClassification1Repository;
import eci.server.DocumentModule.repository.DocClassification2Repository;
import eci.server.DocumentModule.repository.DocTagRepository;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.exception.AttachmentTagNotFoundException;
import eci.server.NewItemModule.exception.ClassificationNotFoundException;
import eci.server.NewItemModule.exception.ClassificationRequiredException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ReleaseModule.dto.ReleaseUpdateRequest;
import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.repository.ReleaseOrganizationReleaseRepository;
import eci.server.ReleaseModule.repository.ReleaseOrganizationRepository;
import eci.server.ReleaseModule.repository.ReleaseTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCreateRequest {
    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification1Id;

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long classification2Id;

    @NotNull(message = "분류 타입을 입력해주세요.")
    private Long tagId;

    @NotNull(message = "이름을 입력해주세요.")
    private String title;

    @Lob
    private String content;

    @Null
    private Long memberId;

    private List<MultipartFile> attachments = new ArrayList<>();


    public static Document toEntity(

            DocumentCreateRequest req,
            DocClassification1Repository classification1,
            DocClassification2Repository classification2,
            MemberRepository memberRepository,
            DocTagRepository docTagRepository

    ){

        if(req.classification1Id==null || req.classification2Id ==null){
            throw new ClassificationRequiredException();
        }


        return  new Document(
                new DocClassification(
                        classification1.findById(req.getClassification1Id())
                                .orElseThrow(
                                        ClassificationNotFoundException::new
                                ),
                        classification2.findById(req.getClassification2Id())
                                .orElseThrow(
                                        ClassificationNotFoundException::new
                                )
                ),

                req.getTitle()==null||req.getTitle().isBlank()
                        ?" ":req.getTitle(),
                req.getContent()==null||req.getContent().isBlank()
                        ?" ":req.getContent(),

                memberRepository.findById(req.getMemberId())
                        .orElseThrow(MemberNotFoundException::new),


                req.attachments.stream().map(
                        i -> new DocumentAttachment(
                                i.getOriginalFilename(),
                                "",
                                //찐 생성이므로 이때 추가되는 문서들 모두 save = true
                                true //save 속성임
                        )
                ).collect(
                        toList()
                ),

                docTagRepository.findById(req.getTagId())
                        .orElseThrow(AttachmentTagNotFoundException::new),

                String.valueOf
                        (req.getTagId() * 1000000 +
                                (int) (Math.random() * 1000)),

                //tempsave
                true,

                //readonly
                true

        );

    }
}
