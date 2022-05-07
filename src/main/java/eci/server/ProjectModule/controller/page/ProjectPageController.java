package eci.server.ProjectModule.controller.page;

import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import eci.server.ProjectModule.dto.project.ProjectMemberRequest;
import eci.server.ProjectModule.dto.project.ProjectSimpleDto;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.stream.Collectors;
@Transactional
@RestController
@RequiredArgsConstructor
public class ProjectPageController {

    /**
     * 프로젝트 모듈에서의 프로젝트 리스트 (내가 만든 프로젝트들 maybe..?)
     */
    @Autowired
    ProjectRepository projectRepository;
    RouteOrderingRepository routeOrderingRepository;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/project/page")
    @AssignMemberId
    public Page<ProjectSimpleDto> pagingProject(@PageableDefault(size=5)
                                                @SortDefault.SortDefaults({
                                                        @SortDefault(
                                                                sort = "createdAt",
                                                                direction = Sort.Direction.DESC)
                                                })
                                                        Pageable pageRequest,
                                                ProjectMemberRequest req) {

        Page<Project> projectList = projectRepository.findAll(pageRequest);

        Page<ProjectSimpleDto> pagingList = projectList.map(
                project -> new ProjectSimpleDto(

                        project.getId(),
                        project.getProjectNumber(),
                        project.getName(),
                        CarTypeDto.toDto(project.getCarType()),

                        ItemProjectDto.toDto(project.getItem()),

                        project.getRevision(),
                        project.getStartPeriod(),
                        project.getOverPeriod(),

                        project.getTempsave(),

                        //tag가 개발
                        project.getProjectAttachments().stream().filter(
                                        a -> a.getTag().equals("DEVELOP")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        ProjectAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),

                        //tag가 디자인
                        project.getProjectAttachments().stream().filter(
                                        a -> a.getTag().equals("DESIGN")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        ProjectAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),


                        project.getCreatedAt(),

                        //project.getLifecycle(), //프로젝트의 라이프사이클

                        routeOrderingRepository.findByItem(project.getItem()).get(-1).getLifecycleStatus(),
                        //아이템의 라이프 사이클

                        req.getMemberId().equals(project.getMember().getId())
                        //현재 로그인 된 플젝 작성멤버랑 같으면 readonly==true

                )
        );

        return pagingList;
    }

}