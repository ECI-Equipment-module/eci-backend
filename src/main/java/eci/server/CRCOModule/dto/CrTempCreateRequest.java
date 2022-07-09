package eci.server.CRCOModule.dto;

import eci.server.CRCOModule.entity.ChangeRequest;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.exception.CrImportanceNotFoundException;
import eci.server.CRCOModule.exception.CrReasonNotFoundException;
import eci.server.CRCOModule.exception.CrSourceNotFoundException;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.exception.ItemNameRequiredException;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrTempCreateRequest {


    private String crNumber;

    private Long crReasonId;

    private String crReason; //없을 경우 직접 입력

    private Long crImportanceId;

    private Long crSourceId;

    @NotNull(message = "cr 이름을 입력해주세요.")
    private String name;

    private String content;

    private String solution;

    @NotNull(message = "아이템 아이디를 입력해주세요.")
    private Long itemId;

    private List<MultipartFile> attachments = new ArrayList<>();

    //attachment tags
    private List<Long> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    @Null
    private Long memberId;

    @Null
    private Long modifierId; //05-22

    //임시저장 예외 처리하기
    public static ChangeRequest toEntity(
            CrTempCreateRequest req,
            Long CrReasonId,
            CrReasonRepository crReasonRepository,
            CrSourceRepository crSourceRepository,
            CrImportanceRepository crImportanceRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,
            NewItemRepository newItemRepository
            ){



        if (req.getTag().size() == 0) {


            return new ChangeRequest(
                    req.getName(),
                    "made when saved",
                    crReasonRepository.findById(CrReasonId).orElseThrow(CrReasonNotFoundException::new),
                    crImportanceRepository.findById(req.getCrImportanceId()).orElseThrow(CrImportanceNotFoundException::new),
                    crSourceRepository.findById(req.getCrSourceId()).orElseThrow(CrSourceNotFoundException::new),
                    req.getContent(),
                    req.getSolution(),
                    newItemRepository.findById(req.getItemId()).orElseThrow(ItemNameRequiredException::new),
                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),
                    true,
                    false

            );
        }
        return new ChangeRequest(
                req.getName(),
                "made when saved",
                crReasonRepository.findById(CrReasonId).orElseThrow(CrReasonNotFoundException::new),
                crImportanceRepository.findById(req.getCrImportanceId()).orElseThrow(CrImportanceNotFoundException::new),
                crSourceRepository.findById(req.getCrSourceId()).orElseThrow(CrSourceNotFoundException::new),
                req.getContent(),
                req.getSolution(),
                newItemRepository.findById(req.getItemId()).orElseThrow(ItemNameRequiredException::new),
                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),
                true,
                false,

                req.attachments.stream().map(
                        i -> new CrAttachment(
                                i.getOriginalFilename(),
                                attachmentTagRepository
                                        .findById(req.getTag().get(req.attachments.indexOf(i))).
                                        orElseThrow(AttachmentNotFoundException::new).getName(),

                                req.getAttachmentComment().isEmpty()?
                                        "":req.getAttachmentComment().get(
                                        req.attachments.indexOf(i)
                                ),
                                false //지금은 임시저장으로 추가되는 문서들 => save = false 다
                        )
                ).collect(
                        toList()
                )

        );
    }
}
