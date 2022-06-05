package eci.server.ProjectModule.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationDto;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationDto;
import eci.server.ProjectModule.dto.projectAttachmentDto.ProjectAttachmentDto;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelDto;
import eci.server.ProjectModule.dto.projectType.ProjectTypeDto;
import eci.server.ProjectModule.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String projectNumber;

    private String clientItemNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate protoStartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate protoOverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p1StartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p1OverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p2StartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p2OverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate sopStartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate sopOverPeriod;

    private ItemProjectDto item;
    private MemberDto member;

    private ProjectTypeDto projectType;
    private ProjectLevelDto projectLevel;
    private ProduceOrganizationDto produceOrganization;
    private ClientOrganizationDto clientOrganization;
    private CarTypeDto carType;
    private List<ProjectAttachmentDto> projectAttachments;

    //private List<RouteOrderingDto> routeDtoList;

    private Long routeId;

    //05-22 추가
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private MemberDto creator;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private MemberDto modifier;

    private Boolean tempsave;
    private boolean readonly;



    public static ProjectDto toDto(
            Project project,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository
    ) {
        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByNewItem(project.getNewItem()),
                        routeProductRepository,
                        routeOrderingRepository
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getProjectNumber(),
                project.getClientItemNumber(),

                project.getProtoStartPeriod(),
                project.getProtoOverPeriod(),

                project.getP1StartPeriod(),
                project.getP1OverPeriod(),

                project.getP2StartPeriod(),
                project.getP2OverPeriod(),

                project.getSopStartPeriod(),
                project.getSopOverPeriod(),

                ItemProjectDto.toDto(project.getNewItem()),
                MemberDto.toDto(project.getMember()),

                ProjectTypeDto.toDto(project.getProjectType()),
                ProjectLevelDto.toDto(project.getProjectLevel()),
                project.getProduceOrganization()==null?null:ProduceOrganizationDto.toDto(project.getProduceOrganization()),
                project.getClientOrganization()==null?null:ClientOrganizationDto.toDto(project.getClientOrganization()),

                CarTypeDto.toDto(project.getCarType()),

                project.getProjectAttachments().
                        stream().
                        map(i -> ProjectAttachmentDto.toDto(i))
                        .collect(toList()),

                //routeDtoList

                //가장 최신의 라우트 오더링 중 최신의 라우트 오더링 아이디
                routeOrderingRepository.findByNewItem(project.getNewItem()).
                        get(routeOrderingRepository.findByNewItem(project.getNewItem()).size() - 1)
                        .getId(),

                //05-22추가
                project.getCreatedAt(),
                MemberDto.toDto(project.getMember()),

                project.getModifier()==null?null:project.getModifiedAt(),
                project.getModifier()==null?null:MemberDto.toDto(project.getModifier()),

                project.getTempsave(),
                project.getReadonly()


        );
    }
}
