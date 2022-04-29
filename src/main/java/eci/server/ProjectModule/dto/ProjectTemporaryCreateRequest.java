package eci.server.ProjectModule.dto;

import eci.server.ItemModule.dto.item.ItemCreateRequest;
import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.exception.item.ColorNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.item.ManufactureNotFoundException;
import eci.server.ItemModule.exception.item.MaterialNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.manufacture.ManufactureRepository;
import eci.server.ItemModule.repository.material.MaterialRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.*;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTemporaryCreateRequest  {

        private String name;

        private Long projectTypeId;

        @Null
        @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
        @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
        private Integer projectNumber;//프로젝트 넘버에 2022-N-projectNumber

        private LocalDate startPeriod;

        private LocalDate overPeriod;

        // 로그인 된 멤버 자동 주입
        @Null
        private Long memberId;

        private Long itemId;

        private List<MultipartFile> attachments = new ArrayList<>();

        private List<String> tag = new ArrayList<>();
        private List<String> attachmentComment = new ArrayList<>();

        private Long projectLevelId;

        private Long clientOrganizationId;

        private Long produceOrganizationId;

        private String carType;

        public static Project toEntity(
                ProjectTemporaryCreateRequest req,
                MemberRepository memberRepository,
                ItemRepository itemRepository,
                ProjectTypeRepository projectTypeRepository,
                ProjectLevelRepository projectLevelRepository,
                ProduceOrganizationRepository produceOrganizationRepository,
                ClientOrganizationRepository clientOrganizationRepository
        ) {
            Integer year = Calendar.getInstance().get(Calendar.YEAR);

            //객체 findById 하는 애들이 빈칸이면
            //임시저장용 인스턴스 id 99999 건네주기

            Long itemId = req.itemId.toString().isBlank()?99999L:req.itemId;
            Long projectTypeId = req.projectTypeId.toString().isBlank()?99999L:req.projectTypeId;
            Long projectLevelId = req.projectLevelId.toString().isBlank()?99999L:req.projectLevelId;
            Long produceOrgId = req.produceOrganizationId.toString().isBlank()?99999L:req.produceOrganizationId;
            Long clientOrgId = req.clientOrganizationId.toString().isBlank()?99999L:req.clientOrganizationId;

            return new Project(
                    req.name.isBlank()?"":req.name,
                    //프로젝트 number은 양산이면 M-현재년도-REQ.NUM / 선형이면 N-~
                    //해당 형식은 스크럼 회의 후 변경
                    "M-"+year.toString()+"-"+"저장 시 생성",

                    req.startPeriod.toString().isBlank()? LocalDate.parse("") :req.startPeriod,
                    req.overPeriod.toString().isBlank()? LocalDate.parse("") :req.overPeriod,

                    //아이템, 프로젝트 타입 등 객체를
                    // 지정하지 않았으면 어쩌지? 임시 객체들을 만들어둬야 하나
                    //-> 그리고 찐 저장 시 해당 객체들이면 제대로 된 객체 지정 경고방식?
                    //TODO 임시아이템 아이디는 ? 일단은 99999로 => 찐 db에선 1로 임시 객체 생성

                    itemRepository.findById(itemId)
                            .orElseThrow(ItemNotFoundException::new),

                    //로그인 된 유저 바로 주입
                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),


                    true,

                    projectTypeRepository.findById(projectTypeId)
                            .orElseThrow(ProjectTypeNotFoundException::new),

                    projectLevelRepository.findById(projectLevelId)
                            .orElseThrow(ProjectLevelNotFoundException::new),

                    produceOrganizationRepository.findById(produceOrgId)
                            .orElseThrow(ProduceOrganizationNotFoundException::new),

                    clientOrganizationRepository.findById(clientOrgId)
                            .orElseThrow(ClientOrganizationNotFoundException::new),

                    req.attachments.stream().map(
                            i -> new ProjectAttachment(
                                    i.getOriginalFilename(),
                                    req.getTag().get(req.attachments.indexOf(i)),
                                    req.getAttachmentComment().get(req.attachments.indexOf(i))
                            )
                    ).collect(
                            toList()
                    ),

                    req.carType.toString().isBlank()?"":req.carType
            );
        }
    }
