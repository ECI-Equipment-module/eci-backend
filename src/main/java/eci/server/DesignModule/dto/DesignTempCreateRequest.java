package eci.server.DesignModule.dto;

import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ProjectModule.exception.*;
import eci.server.ProjectModule.repository.project.ProjectRepository;

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
    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();


    public static Design toEntity(
            DesignTempCreateRequest req,
            MemberRepository memberRepository,
            ItemRepository itemRepository
    ) {


        Long itemId = req.itemId==null?99999L:req.itemId;

        if(req.getTag().size()>0) {
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
                                    req.getTag().get(req.attachments.indexOf(i)),
                                    req.getAttachmentComment().get(req.attachments.indexOf(i))
                            )
                    ).collect(
                            toList()
                    )

                    //Project 생성자에 들이밀기

            );
        }

        /**
         * attachment 없을 시
         */

        return new Design(

                itemRepository.findById(itemId)
                        .orElseThrow(ProjectNotFoundException::new),

                //로그인 된 유저 바로 주입
                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),

                true,
                false

        );
    }
}


