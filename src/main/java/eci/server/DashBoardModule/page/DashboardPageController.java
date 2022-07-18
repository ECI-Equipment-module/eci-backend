package eci.server.DashBoardModule.page;

import eci.server.DashBoardModule.dto.myProject.ProjectDashboardDto;
import eci.server.ProjectModule.dto.project.ProjectMemberRequest;
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
public class DashboardPageController {


    //이걸로 프로젝트의 라우트 오더링, 프로덕트에 해당하는 아이들을 찾아서 데려올 것이다
    @Autowired
    ProjectService projectService;
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @GetMapping("dashboard/project/page")
    @AssignMemberId //작성한 멤버
    public Page<ProjectDashboardDto> pagingDashboardProject
            (@PageableDefault(size=5)
             @SortDefault.SortDefaults({
                     @SortDefault(
                             sort = "createdAt",
                             direction = Sort.Direction.DESC
                     )
             }
             )
             Pageable pageRequest,
             ProjectMemberRequest req) {
        Page<ProjectDashboardDto> projectDashboardDtos = projectService.readDashboard(
                pageRequest, req
        );
        return projectDashboardDtos;
    }
}