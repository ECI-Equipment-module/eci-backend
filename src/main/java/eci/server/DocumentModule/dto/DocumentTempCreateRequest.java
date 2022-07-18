package eci.server.DocumentModule.dto;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTempCreateRequest{

    private Long classification1Id;

    private Long classification2Id;

    private Long tagId;

    private String title;

    @Lob
    private String content;

    @Null
    private Long memberId;

    private List<MultipartFile> attachments = new ArrayList<>();

    public static Document toEntity(

            DocumentTempCreateRequest req,
            DocClassification1Repository classification1,
            DocClassification2Repository classification2,
            MemberRepository memberRepository,
            DocTagRepository docTagRepository

    ){


        return  new Document(


                req.getClassification1Id()==null?
                        null:
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


                req.getAttachments()==null?
                        null
                :req.getAttachments().stream().map(
                        i -> new DocumentAttachment(
                                i.getOriginalFilename(),
                                "",
                                //찐 생성이므로 이때 추가되는 문서들 모두 save = true
                                true //save 속성임
                        )
                ).collect(
                        toList()
                ),


                req.getTagId()==null?
                        null:
                docTagRepository.findById(req.getTagId())
                        .orElseThrow(AttachmentTagNotFoundException::new),

                "made when saved",
                //tempsave
                true,
                //readonly
                false
        );

    }

}

