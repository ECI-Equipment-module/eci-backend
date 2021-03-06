package eci.server.DesignModule.dto;

import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.exception.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignTempCreateRequest {

    private Long itemId;

    @Null
    private Long memberId;

    private List<MultipartFile> attachments = new ArrayList<>();
    private List<Long> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();

    //단순 시연용
    private String designContent;

    public static Design toEntity(
            DesignTempCreateRequest req,
            MemberRepository memberRepository,
            NewItemRepository itemRepository,
            AttachmentTagRepository attachmentTagRepository
    ) {


        Long itemId = req.itemId==null?99999L:req.itemId;


        /**
         * attachment 있을 시
         */
        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            return new Design(
                    itemRepository.findById(itemId)
                            .orElseThrow(ItemNotFoundException::new),

                    //로그인 된 유저 바로 주입
                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true,
                    false, //임시저장은 readonly false //05-12 수정사항반영

                    req.attachments.stream().map(

                            i -> new DesignAttachment(
                                    i.getOriginalFilename(),
                                    attachmentTagRepository
                                            .findById(req.getTag().get(req.attachments.indexOf(i))).
                                            orElseThrow(AttachmentNotFoundException::new).getName(),
                                    req.getAttachmentComment().isEmpty()?
                                            "":
                                            req.getAttachmentComment().
                                                    get(req.attachments.indexOf(i)).
                                                    isBlank()?"":
                                                    req.getAttachmentComment().get(req.attachments.indexOf(i)),
                                    false
                            )

                    ).collect(
                            toList()
                    ),

                    //Project 생성자에 들이밀기

                    req.getDesignContent() //단순 시연용

            );
        }


        /**
         * attachment 없을 시
         */
        return new Design(

                itemRepository.findById(itemId)
                        .orElseThrow(ItemNotFoundException::new),

                //로그인 된 유저 바로 주입
                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                true,
                false,

                req.getDesignContent() //단순 시연용

        );
    }
}


