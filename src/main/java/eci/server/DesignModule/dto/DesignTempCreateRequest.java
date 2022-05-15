package eci.server.DesignModule.dto;

import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ProjectModule.dto.project.ProjectTemporaryCreateRequest;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.*;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignTempCreateRequest {

    private String name;

    private Long projectId;

    @Null
    private Long memberId;

    private List<MultipartFile> attachments = new ArrayList<>();
    private List<String> tag = new ArrayList<>();
    private List<String> attachmentComment = new ArrayList<>();


    public static Design toEntity(
            DesignTempCreateRequest req,
            MemberRepository memberRepository,
            ProjectRepository projectRepository
    ) {


        Long projectId = req.projectId==null?99999L:req.projectId;

        if(req.getTag().size()>0) {
            return new Design(
                    req.name.isBlank() ? " default " : req.name,
                    //프로젝트 number은 양산이면 M-현재년도-REQ.NUM / 선형이면 N-~
                    //해당 형식은 스크럼 회의 후 변경

                    projectRepository.findById(projectId)
                            .orElseThrow(ProjectNotFoundException::new),

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
                req.name.isBlank() ? " default " : req.name,
                //프로젝트 number은 양산이면 M-현재년도-REQ.NUM / 선형이면 N-~
                //해당 형식은 스크럼 회의 후 변경

                projectRepository.findById(projectId)
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


