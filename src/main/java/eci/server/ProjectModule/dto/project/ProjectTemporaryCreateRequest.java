package eci.server.ProjectModule.dto.project;

import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        private String clientItemNumber;

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

        private Long itemId;

        private List<MultipartFile> attachments = new ArrayList<>();

        private List<Long> tag = new ArrayList<>();
        private List<String> attachmentComment = new ArrayList<>();

        private Long projectLevelId;

        private Long clientOrganizationId;

        private Long produceOrganizationId;

        private Long carType;

        public static Project toEntity(
                ProjectTemporaryCreateRequest req,
                MemberRepository memberRepository,
                NewItemRepository itemRepository,
                ProjectTypeRepository projectTypeRepository,
                ProjectLevelRepository projectLevelRepository,
                ProduceOrganizationRepository produceOrganizationRepository,
                ClientOrganizationRepository clientOrganizationRepository,
                CarTypeRepository carTypeRepository,
                AttachmentTagRepository attachmentTagRepository
        ) {
            

            Integer year = Calendar.getInstance().get(Calendar.YEAR);

            //객체 findById 하는 애들이 빈칸이면
            //임시저장용 인스턴스 id 99999 건네주기

            Long itemId = req.itemId==null?99999L:req.itemId;
            Long projectTypeId = req.projectTypeId==null?99999L:req.projectTypeId;
            Long projectLevelId = req.projectLevelId==null?99999L:req.projectLevelId;
            Long produceOrgId = req.produceOrganizationId==null?99999L:req.produceOrganizationId;
            Long clientOrgId = req.clientOrganizationId==null?99999L:req.clientOrganizationId;
            Long carTypeId = req.carType==null?99999L:req.carType;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //System.out.println(LocalDate.now().format(formatter));

            if(req.getTag().size()>0) {
                return new Project(
                        req.name.isBlank() ? " default " : req.name,
                        //프로젝트 number은 양산이면 M-현재년도-REQ.NUM / 선형이면 N-~
                        //해당 형식은 스크럼 회의 후 변경
                        "M-" + year.toString() + "-" + "저장 시 생성",

                        req.clientItemNumber,

                        req.protoStartPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.protoStartPeriod, DateTimeFormatter.ISO_DATE),

                        req.protoOverPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.protoOverPeriod, DateTimeFormatter.ISO_DATE),

                        req.p1StartPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.p1StartPeriod, DateTimeFormatter.ISO_DATE),

                        req.p1OverPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.p1OverPeriod, DateTimeFormatter.ISO_DATE),

                        req.p2StartPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.p2StartPeriod, DateTimeFormatter.ISO_DATE),

                        req.p2OverPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.p2OverPeriod, DateTimeFormatter.ISO_DATE),

                        req.sopStartPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.sopStartPeriod, DateTimeFormatter.ISO_DATE),

                        req.sopOverPeriod.toString().isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                                LocalDate.parse(req.sopOverPeriod, DateTimeFormatter.ISO_DATE),


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

                        false, //임시저장은 readonly false //05-12 수정사항반영

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
                                        attachmentTagRepository
                                                .findById(req.getTag().get(req.attachments.indexOf(i))).
                                                orElseThrow(AttachmentNotFoundException::new).getName(),
                                        req.getAttachmentComment().get(req.attachments.indexOf(i))
                                )
                        ).collect(
                                toList()
                        ),

                        //Project 생성자에 들이밀기

                        carTypeRepository.findById(req.carType)
                                .orElseThrow(ClientOrganizationNotFoundException::new)

//                    req.carType.toString().isBlank()?"":req.carType

                );
            }

            /**
             * attachment 없을 시
             */

            return new Project(
                    req.name.isBlank() ? " default " : req.name,
                    //프로젝트 number은 양산이면 M-현재년도-REQ.NUM / 선형이면 N-~
                    //해당 형식은 스크럼 회의 후 변경
                    "M-" + year.toString() + "-" + "저장 시 생성",

                    req.clientItemNumber,


                    req.protoStartPeriod == null || req.protoStartPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.protoStartPeriod, DateTimeFormatter.ISO_DATE),

                    req.protoOverPeriod == null || req.protoOverPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.protoOverPeriod, DateTimeFormatter.ISO_DATE),

                    req.p1StartPeriod ==null || req.p1StartPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.p1StartPeriod, DateTimeFormatter.ISO_DATE),

                    req.p1OverPeriod==null || req.p1OverPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.p1OverPeriod, DateTimeFormatter.ISO_DATE),

                    req.p2StartPeriod ==null || req.p2StartPeriod.isBlank()  ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.p2StartPeriod, DateTimeFormatter.ISO_DATE),

                    req.p2OverPeriod == null || req.p2OverPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.p2OverPeriod, DateTimeFormatter.ISO_DATE),

                    req.sopStartPeriod== null || req.sopStartPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.sopStartPeriod, DateTimeFormatter.ISO_DATE),

                    req.sopOverPeriod==null || req.sopOverPeriod.isBlank() ? LocalDate.parse(LocalDate.now().format(formatter)) :
                            LocalDate.parse(req.sopOverPeriod, DateTimeFormatter.ISO_DATE),

                    //아이템, 프로젝트 타입 등 객체를
                    // 지정하지 않았으면 어쩌지? 임시 객체들을 만들어둬야 하나
                    //-> 그리고 찐 저장 시 해당 객체들이면 제대로 된 객체 지정 경고방식?
                    //TODO 임시아이템 아이디는 ? 일단은 99999로 => 찐 db에선 1로 임시 객체 생성

                   itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new),

                    //로그인 된 유저 바로 주입
                    memberRepository.findById(
                            req.getMemberId()
                    ).orElseThrow(MemberNotFoundException::new),

                    true,
                    false,

                    req.projectTypeId==null?projectTypeRepository.findById(99999L)
                            .orElseThrow(ProjectTypeNotFoundException::new)
                            :projectTypeRepository.findById(projectTypeId)
                            .orElseThrow(ProjectTypeNotFoundException::new),

                    req.projectLevelId==null?
                            projectLevelRepository.findById(99999L)
                                    .orElseThrow(ProjectLevelNotFoundException::new):
                            projectLevelRepository.findById(projectLevelId)
                            .orElseThrow(ProjectLevelNotFoundException::new),

                    req.produceOrganizationId==null?
                            produceOrganizationRepository.findById(99999L)
                                    .orElseThrow(ProduceOrganizationNotFoundException::new)
                            :
                            produceOrganizationRepository.findById(produceOrgId)
                            .orElseThrow(ProduceOrganizationNotFoundException::new),

                    clientOrganizationRepository.findById(clientOrgId)
                            .orElseThrow(ClientOrganizationNotFoundException::new),

                    //Project 생성자에 들이밀기

                    carTypeRepository.findById(carTypeId)
                            .orElseThrow(CarTypeNotFoundException::new)

//                    req.carType.toString().isBlank()?"":req.carType

            );
        }
    }
