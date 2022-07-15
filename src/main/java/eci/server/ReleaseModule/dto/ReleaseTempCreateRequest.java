package eci.server.ReleaseModule.dto;

import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ReleaseModule.entity.Release;
import eci.server.ReleaseModule.entity.ReleaseAttachment;
import eci.server.ReleaseModule.exception.ReleaseNeedsTargetException;
import eci.server.ReleaseModule.exception.ReleaseNotFoundExcpetion;
import eci.server.ReleaseModule.exception.ReleaseOrganizationNotFoundException;
import eci.server.ReleaseModule.exception.ReleaseTypeNotEmptyException;
import eci.server.ReleaseModule.repository.ReleaseOrganizationRepository;
import eci.server.ReleaseModule.repository.ReleaseTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseTempCreateRequest{

    private Long id;
    private String releaseNumber;

    private Long releaseType; //{id,name}으로 해야할 듯 합니다. id는 OK

    private Long releaseItemId;

    private Long releaseCoId;

    private String releaseTitle;

    private String releaseContent;

    private List<Long> releaseOrganizationId;

    private List<MultipartFile> attachments = new ArrayList<>();

    //attachment tags
    private List<Long> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    @Null
    private Long memberId;


    public static Release toEntity(
            ReleaseTempCreateRequest req,
            MemberRepository memberRepository,
            NewItemRepository newItemRepository,
            ChangeOrderRepository changeOrderRepository,
            ReleaseTypeRepository releaseTypeRepository,
            ReleaseOrganizationRepository releaseOrganizationRepository,
            AttachmentTagRepository attachmentTagRepository
    ) {


        if (req.tag.size() == 0) {
            return new Release(

                    req.getReleaseTitle(),

                    req.getReleaseContent(),

                    "made when saved",

                    req.getReleaseItemId()==null?
                            null:newItemRepository.findById(
                            req.getReleaseItemId()
                    ).orElseThrow(ItemNotFoundException::new),

                    req.getReleaseCoId()==null?
                            null:changeOrderRepository.findById(
                            req.getReleaseCoId()
                    ).orElseThrow(ReleaseNotFoundExcpetion::new),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true,
                    true, //readonly default - false, create 하면 true

                    req.getReleaseType()==null?
                            null:releaseTypeRepository.findById(req.getReleaseType()).
                            orElseThrow(ReleaseTypeNotEmptyException::new),

                    req.getReleaseOrganizationId().size()==0?
                            null:
                            req.getReleaseOrganizationId().stream().map(
                                    i-> (
                                            releaseOrganizationRepository
                                                    .findById(i)
                                                    .orElseThrow(ReleaseOrganizationNotFoundException::new)
                                    )
                            ).collect(toList())
            );

        } else {

            return new Release(

                    req.getReleaseTitle(),

                    req.getReleaseContent(),

                    "made when saved",

                    req.getReleaseItemId()==null?
                            null:newItemRepository.findById(
                            req.getReleaseItemId()
                    ).orElseThrow(ItemNotFoundException::new),

                    req.getReleaseCoId()==null?
                            null:changeOrderRepository.findById(
                            req.getReleaseCoId()
                    ).orElseThrow(ReleaseNotFoundExcpetion::new),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true,
                    true, //readonly default - false, create 하면 true

                    req.getReleaseType()==null?
                            null:releaseTypeRepository.findById(req.getReleaseType()).
                            orElseThrow(ReleaseTypeNotEmptyException::new),

                    req.getReleaseOrganizationId().size()==0?
                            null:
                            req.getReleaseOrganizationId().stream().map(
                                    i-> (
                                            releaseOrganizationRepository
                                                    .findById(i)
                                                    .orElseThrow(ReleaseOrganizationNotFoundException::new)
                                    )
                            ).collect(toList()),

                    req.attachments.stream().map(
                            i -> new ReleaseAttachment(
                                    i.getOriginalFilename(),
                                    attachmentTagRepository
                                            .findById(req.getTag().get(req.attachments.indexOf(i))).
                                            orElseThrow(AttachmentNotFoundException::new).getName(),
                                    req.getAttachmentComment().isEmpty()?
                                            "":
                                            req.getAttachmentComment().isEmpty()?
                                                    "":req.getAttachmentComment().get(
                                                    req.attachments.indexOf(i)
                                            ),
                                    false
                            )
                    ).collect(
                            toList()
                    )
            );

        }
    }

}

