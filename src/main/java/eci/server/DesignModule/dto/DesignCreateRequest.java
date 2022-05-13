package eci.server.DesignModule.dto;

import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ProjectModule.dto.project.ProjectCreateRequest;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignCreateRequest {

    @NotNull(message = "디자인 이름을 입력해주세요.")
    private String name;

    // 로그인 된 멤버 자동 주입
    @Null
    private Long memberId;

    @NotNull(message = "디자인과 연결된 프로젝트 아이디를 입력해주세요.")
    private Long projectId;

    private List<MultipartFile> attachments = new ArrayList<>();

    //attachment tags
    private List<String> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();


    public static Design toEntity(
            DesignCreateRequest req,
            MemberRepository memberRepository,
            ProjectRepository projectRepository
    ) {


        if (req.tag.size() == 0) { //Project에 Attachment 존재하지 않을 시에 생성자
            return new Design(
                    req.name,

                    projectRepository.findById(req.getProjectId())
                            .orElseThrow(ProjectNotFoundException::new),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true, //05-12 수정사항 반영 - 라우트까지 작성되어야 false
                    true//readonly default - false, create 하면 true
            );

        } else {

            return new Design(
                    req.name,

                    projectRepository.findById(req.getProjectId())
                            .orElseThrow(ProjectNotFoundException::new),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true, //05-12 수정사항 반영 - 라우트까지 작성되어야 false
                    true, //readonly default - false, create 하면 true

                    req.attachments.stream().map(
                            i -> new ProjectAttachment(
                                    i.getOriginalFilename(),
                                    req.getTag().get(req.attachments.indexOf(i)),
                                    req.getAttachmentComment().isEmpty()?
                                            "":
                                            req.getAttachmentComment().
                                                    get(req.attachments.indexOf(i)).
                                                    isBlank()?"":
                                                    req.getAttachmentComment().get(req.attachments.indexOf(i))
                            )
                    ).collect(
                            toList()
                    )
            );
        }
    }
}
