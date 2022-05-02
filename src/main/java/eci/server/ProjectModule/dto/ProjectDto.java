package eci.server.ProjectModule.dto;

import eci.server.ItemModule.dto.item.ItemDto;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.RouteOrderingDto;
import eci.server.ItemModule.dto.newRoute.RouteProductDto;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationDto;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationDto;
import eci.server.ProjectModule.dto.projectAttachmentDto.ProjectAttachmentDto;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelDto;
import eci.server.ProjectModule.dto.projectType.ProjectTypeDto;
import eci.server.ProjectModule.entity.project.*;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String projectNumber;
    private LocalDate startPeriod;
    private LocalDate overPeriod;
    private ItemProjectDto item;
    private MemberDto member;
    private Boolean tempsave;
    private ProjectTypeDto projectType;
    private ProjectLevelDto projectLevel;
    private ProduceOrganizationDto produceOrganization;
    private ClientOrganizationDto clientOrganization;
    private String carType;
    private List<ProjectAttachmentDto> projectAttachments;

    private List<RouteOrderingDto> routeDtoList;

    public static ProjectDto toDto(
            Project project,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository
    ){
        List <RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByItem(project.getItem()),
                        routeProductRepository,
                        routeOrderingRepository
                )
        ).orElseThrow(RouteNotFoundException::new);

        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getProjectNumber(),
                project.getStartPeriod(),
                project.getOverPeriod(),
                ItemProjectDto.toDto(project.getItem()),
                MemberDto.toDto(project.getMember()),
                project.getTempsave(),
                ProjectTypeDto.toDto(project.getProjectType()),
                ProjectLevelDto.toDto(project.getProjectLevel()),
                ProduceOrganizationDto.toDto(project.getProduceOrganization()),
                ClientOrganizationDto.toDto(project.getClientOrganization()),
                project.getCarType(),
                project.getProjectAttachments().
                        stream().
                        map(i -> ProjectAttachmentDto.toDto(i))
                        .collect(toList()),

                routeDtoList

        );
    }

}