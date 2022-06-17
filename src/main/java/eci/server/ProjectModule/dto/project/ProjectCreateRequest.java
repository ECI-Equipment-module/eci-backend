package eci.server.ProjectModule.dto.project;

import eci.server.ItemModule.exception.item.*;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.*;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {

    @NotNull(message = "프로젝트 이름을 입력해주세요.")
    private String name;


    @NotNull(message = "프로젝트 타입을 입력해주세요.")
    private Long projectTypeId;

    private String protoStartPeriod;

    private String protoOverPeriod;

    private String p1StartPeriod;

    private String p1OverPeriod;

    private String p2StartPeriod;

    private String p2OverPeriod;

    private String sopStartPeriod;

    private String sopOverPeriod;
    // 로그인 된 멤버 자동 주입
    @Null
    private Long memberId;

    @NotNull(message = "아이템 아이디를 입력해주세요.")
    private Long itemId;

    private List<MultipartFile> attachments = new ArrayList<>();

    //attachment tags
    private List<Long> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    @NotNull(message = "프로젝트 레벨을 입력해주세요.")
    private Long projectLevelId;

    private Long clientOrganizationId; //양산 아니면 없어도 됨

    //@NotNull(message = "생산사를 입력해주세요.")
    private Long produceOrganizationId;

    @NotNull(message = "차종을 입력해주세요.")
    private Long carType;

    private String clientItemNumber;


    public static Project toEntity(
            ProjectCreateRequest req,
            MemberRepository memberRepository,
            NewItemRepository itemRepository,
            ProjectTypeRepository projectTypeRepository,
            ProjectLevelRepository projectLevelRepository,
            ProduceOrganizationRepository produceOrganizationRepository,
            ClientOrganizationRepository clientOrganizationRepository,
            CarTypeRepository carTypeRepository,
            AttachmentTagRepository attachmentTagRepository
            ) {

        if(req.getProjectLevelId()==null || req.getProjectLevelId()==99999L){
            throw new ProjectLevelNotEmptyException();
        }
        else if (req.getCarType() == null || req.getCarType() == 99999L){
            throw new CarTypeNotEmptyException();
        }
        else if(req.getProjectTypeId()==null || req.getProjectTypeId()==99999L){
            throw new ProjectTypeNotEmptyException();
        }
        else if(req.getItemId()==null || req.getItemId()==99999L){
            throw new ItemNotFoundException();
        }
        else if(req.getName() == null || req.getName().isBlank()){
            throw new NameNotEmptyException();
        }

//        //양산 개발의 아이디가 1 (Long)
//        if (req.projectTypeId.equals(1L)) {
//            //TODO 프로젝트 타입이 양산개발이라면 필수 속성은 고객사 / 차종 은 null 이 되면 안됨 검사
//            if (req.clientOrganizationId == null || req.carType== null)
//                throw new MassProductionSaveException();
//
//        } else if ( //필수 속성 중 저장 안된 부분 있나 체크
//            //TODO 필수 속성 부분 체크 needed
//                req.projectTypeId.toString().isBlank() ||
//                        req.projectLevelId.toString().isBlank() ||
//                        req.name.isBlank() || req.itemId.toString().isBlank()
//        ) {
//
//            throw new ProjectCreateNotEmptyException();
//        }


        String finalProjNum = ProjectNumber(req.getProjectTypeId());

        if (req.tag.size() == 0) { //Project에 Attachment 존재하지 않을 시에 생성자
            return new Project(
                    req.name,
                    //TODO 임시 : 프로젝트 number은 양산이면  M-현재년도-REQ.NUM / 선형이면 N-~
                    //해당 형식 스크럼 회의 후 변경 예정


                    finalProjNum,

                    req.clientItemNumber.isEmpty()?null:req.clientItemNumber,

                    req.protoStartPeriod.isEmpty()?null:LocalDate.parse(req.protoStartPeriod, DateTimeFormatter.ISO_DATE),
                    req.protoOverPeriod.isEmpty()?null:LocalDate.parse(req.protoOverPeriod, DateTimeFormatter.ISO_DATE),

                    req.p1StartPeriod.isEmpty()?null:LocalDate.parse(req.p1StartPeriod, DateTimeFormatter.ISO_DATE),
                    req.p1OverPeriod.isEmpty()?null:LocalDate.parse(req.p1OverPeriod, DateTimeFormatter.ISO_DATE),

                    req.p2StartPeriod.isEmpty()?null:LocalDate.parse(req.p2StartPeriod, DateTimeFormatter.ISO_DATE),
                    req.p2OverPeriod.isEmpty()?null:LocalDate.parse(req.p2OverPeriod, DateTimeFormatter.ISO_DATE),

                    req.sopStartPeriod.isEmpty()?null:LocalDate.parse(req.sopStartPeriod, DateTimeFormatter.ISO_DATE),
                    req.sopOverPeriod.isEmpty()?null:LocalDate.parse(req.sopOverPeriod, DateTimeFormatter.ISO_DATE),

                    itemRepository.findById(req.itemId)
                            .orElseThrow(ItemNotFoundException::new),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),


                    true, //05-12 수정사항 반영
                    true, //readonly default - false, create 하면 true

                    projectTypeRepository.findById(req.projectTypeId)
                            .orElseThrow(ProjectTypeNotFoundException::new),

                    projectLevelRepository.findById(req.projectLevelId)
                            .orElseThrow(ProjectLevelNotFoundException::new),
                    // 예외 만들기
                    req.produceOrganizationId==null?null:
                            produceOrganizationRepository.findById(req.produceOrganizationId)
                            .orElseThrow(ProduceOrganizationNotFoundException::new),
                    // 예외 만들기
                    req.getClientOrganizationId()==null?null:
                            clientOrganizationRepository.findById(req.getClientOrganizationId())
                            .orElseThrow(ClientOrganizationNotFoundException::new),
                    // 예외 만들기


                    carTypeRepository.findById(req.carType)
                            .orElseThrow(CarTypeNotFoundException::new)
            );

        } else {

            return new Project(
                    req.name,
                    //TODO 임시 : 프로젝트 number은 양산이면  M-현재년도-REQ.NUM / 선형이면 N-~
                    //해당 형식 스크럼 회의 후 변경 예정


                    finalProjNum,

                    req.clientItemNumber,

                    req.protoStartPeriod.isEmpty()?null:LocalDate.parse(req.protoStartPeriod, DateTimeFormatter.ISO_DATE),
                    req.protoOverPeriod.isEmpty()?null:LocalDate.parse(req.protoOverPeriod, DateTimeFormatter.ISO_DATE),

                    req.p1StartPeriod.isEmpty()?null:LocalDate.parse(req.p1StartPeriod, DateTimeFormatter.ISO_DATE),
                    req.p1OverPeriod.isEmpty()?null:LocalDate.parse(req.p1OverPeriod, DateTimeFormatter.ISO_DATE),

                    req.p2StartPeriod.isEmpty()?null:LocalDate.parse(req.p2StartPeriod, DateTimeFormatter.ISO_DATE),
                    req.p2OverPeriod.isEmpty()?null:LocalDate.parse(req.p2OverPeriod, DateTimeFormatter.ISO_DATE),

                    req.sopStartPeriod.isEmpty()?null:LocalDate.parse(req.sopStartPeriod, DateTimeFormatter.ISO_DATE),
                    req.sopOverPeriod.isEmpty()?null:LocalDate.parse(req.sopOverPeriod, DateTimeFormatter.ISO_DATE),

                    itemRepository.findById(req.itemId)
                            .orElseThrow(ItemNotFoundException::new),

                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),


                    true, //05-12 수정사항 반영
                    true, //저장 시엔 readonly = true


                    projectTypeRepository.findById(req.projectTypeId)
                            .orElseThrow(ProjectTypeNotFoundException::new),

                    projectLevelRepository.findById(req.projectLevelId)
                            .orElseThrow(ProjectLevelNotFoundException::new),
                    // 예외 만들기
                    req.produceOrganizationId==null?null:
                    produceOrganizationRepository.findById(req.produceOrganizationId)
                            .orElseThrow(ProduceOrganizationNotFoundException::new),
                    // 예외 만들기

                    req.getClientOrganizationId()==null?null:
                            clientOrganizationRepository.findById(req.getClientOrganizationId())
                            .orElseThrow(ClientOrganizationNotFoundException::new),
                    // 예외 만들기

                    req.attachments.stream().map(
                            i -> new ProjectAttachment(
                                    i.getOriginalFilename(),
                                    attachmentTagRepository
                                            .findById(req.getTag().get(req.attachments.indexOf(i))).
                                            orElseThrow(AttachmentNotFoundException::new).getName(),
                                    req.getAttachmentComment().isEmpty()?
                                            "":
                                    req.getAttachmentComment().
                                            get(req.attachments.indexOf(i)).
                                            isBlank()?"":
                                            req.getAttachmentComment().get(req.attachments.indexOf(i))
                            )
                    ).collect(
                            toList()
                    ),

                    carTypeRepository.findById(req.carType)
                            .orElseThrow(CarTypeNotFoundException::new)
            );
        }
    }

    public static String ProjectNumber(Long ProjectLevelId){
        Integer year = Calendar.getInstance().get(Calendar.YEAR);

        Date now = new Date();
        //projectNum 겹치지않도록 설정(순간의 연-월시분초
        String projectNum = new SimpleDateFormat("MMddHHmmss", Locale.ENGLISH).format(now);
        String finalProjNumber = new String();

        if (ProjectLevelId.equals(1L)) {
            finalProjNumber = "M-" + year + "-" + projectNum;
        } else {
            finalProjNumber= "N-" + year + "-" + projectNum;
        }

        return finalProjNumber;
    }
}
