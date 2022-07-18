package eci.server.ProjectModule.controller.page;

import eci.server.DashBoardModule.dto.myProject.ProjectDashboardDto;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ProjectModule.dto.project.ProjectMemberRequest;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.service.ProjectService;
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

@Transactional
@RestController
@RequiredArgsConstructor
public class ProjectPageController {
    private final ProjectService projectService;
    /**
     * 프로젝트 모듈에서의 프로젝트 리스트 (내가 만든 프로젝트들 maybe..?)
     */
    @Autowired
    ProjectRepository projectRepository;
    RouteOrderingRepository routeOrderingRepository;
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/project/page")
    @AssignMemberId
    public Page<ProjectDashboardDto> pagingProject(@PageableDefault(size=5)
                                                @SortDefault.SortDefaults({
                                                        @SortDefault(
                                                                sort = "createdAt",
                                                                direction = Sort.Direction.DESC)
                                                })
                                                        Pageable pageRequest,
                                                   ProjectMemberRequest req) {



        Page<ProjectDashboardDto> projectDashboardDtos = projectService.readPageAll(pageRequest, req);


        return projectDashboardDtos;
    }

}