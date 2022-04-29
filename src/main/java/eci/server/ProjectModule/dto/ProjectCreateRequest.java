package eci.server.ProjectModule.dto;

import eci.server.ItemModule.entity.item.*;
import eci.server.ItemModule.exception.item.*;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.manufacture.ManufactureRepository;
import eci.server.ItemModule.repository.material.MaterialRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
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
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {

    @NotNull@NotBlank(message = "프로젝트 이름을 입력해주세요.")
    private String name;


    @NotNull(message = "프로젝트 타입을 입력해주세요.")
    private Long projectTypeId;

    @Null
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Integer projectNumber;//프로젝트 넘버에 2022-N-projectNumber

    @NotNull(message = "시작 시기를 입력해주세요.")
    private LocalDate startPeriod;

    @NotNull(message = "종료 시기를 입력해주세요.")
    private LocalDate overPeriod;

    // 로그인 된 멤버 자동 주입
    @Null
    private Long memberId;

    @NotNull(message = "아이템 아이디를 입력해주세요.")
    private Long itemId;

    private List<MultipartFile> attachments = new ArrayList<>();

    //attachment tags
    private List<String> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    @NotNull(message = "프로젝트 레벨을 입력해주세요.")
    private Long projectLevelId;

    @NotNull(message = "고객사를 입력해주세요.")
    private Long clientOrganizationId;

    @NotNull(message = "생산사를 입력해주세요.")
    private Long produceOrganizationId;

    @NotNull(message = "차종을 입력해주세요.")
    private String carType;

    public static Project toEntity(
            ProjectCreateRequest req,
            MemberRepository memberRepository,
            ItemRepository itemRepository,
            ProjectTypeRepository projectTypeRepository,
            ProjectLevelRepository projectLevelRepository,
            ProduceOrganizationRepository produceOrganizationRepository,
            ClientOrganizationRepository clientOrganizationRepository
            ) {


        //양산 개발의 아이디가 1 (Long)
        if (req.projectTypeId.equals(1L)){
            //TODO 프로젝트 타입이 양산개발이라면 필수 속성은 고객사 / 차종 은 null 이 되면 안됨 검사
            if(req.clientOrganizationId.toString().isBlank()&&req.carType.isEmpty())
            throw new MassProductionSaveException();

        }else if( //필수 속성 중 저장 안된 부분 있나 체크
                //TODO 필수 속성 부분 체크 needed
                        req.projectTypeId.toString().isBlank()||
                        req.projectLevelId.toString().isBlank()||
                        req.name.isBlank()|| req.itemId.toString().isBlank()
        ){
            throw new ProjectCreateNotEmptyException();
        }

        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        String projectNum = String.format("%05d", req.projectNumber);

        return new Project(
                req.name,
                //TODO 임시 : 프로젝트 number은 양산이면  M-현재년도-REQ.NUM / 선형이면 N-~
                //해당 형식 스크럼 회의 후 변경 예정
                "M-"+year.toString()+"-"+projectNum,

                req.startPeriod,
                req.overPeriod,

                itemRepository.findById(req.itemId)
                        .orElseThrow(ItemNotFoundException::new),

                memberRepository.findById(
                        req.getMemberId()
                ).orElseThrow(MemberNotFoundException::new),


                false,

                projectTypeRepository.findById(req.projectTypeId)
                        .orElseThrow(ProjectTypeNotFoundException::new),

                projectLevelRepository.findById(req.projectLevelId)
                        .orElseThrow(ProjectLevelNotFoundException::new),
                // 예외 만들기
                produceOrganizationRepository.findById(req.produceOrganizationId)
                        .orElseThrow(ProduceOrganizationNotFoundException::new),
                // 예외 만들기

                clientOrganizationRepository.findById(req.clientOrganizationId)
                        .orElseThrow(ClientOrganizationNotFoundException::new),
                // 예외 만들기

                req.attachments.stream().map(
                        i -> new ProjectAttachment(
                                i.getOriginalFilename(),
                                req.getTag().get(req.attachments.indexOf(i)),
                                req.getAttachmentComment().get(req.attachments.indexOf(i))
                        )
                ).collect(
                        toList()
                ),

                req.carType
        );
    }
}
