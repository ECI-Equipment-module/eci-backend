package eci.server.ProjectModule.controller.project;

import eci.server.ItemModule.dto.member.MemberReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.ProjectCreateRequest;
import eci.server.ProjectModule.dto.ProjectReadCondition;
import eci.server.ProjectModule.dto.ProjectTemporaryCreateRequest;
import eci.server.ProjectModule.dto.ProjectUpdateRequest;
import eci.server.ProjectModule.service.ProjectService;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://localhost:3000")
public class ProjectController {

    private final ProjectService projectService;

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/project/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response tempCreate(
            @Valid @ModelAttribute
                    ProjectTemporaryCreateRequest req
    ) {

        return Response.success(

                projectService.tempCreate(req));
    }


    /**
     * 프로젝트 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/project")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(
            @Valid @ModelAttribute
                    ProjectCreateRequest req
    ) {

        return Response.success(

                projectService.create(req));
    }

    /**
     * 특정 프로젝트 수정
     *
     * @param id
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProjectUpdateRequest req) {

        return Response.success(projectService.update(id, req));
    }

    @GetMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(projectService.read(id));
    }


    @GetMapping("/project")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ProjectReadCondition cond) {
        return Response.success(projectService.readAll(cond));
    }

    @DeleteMapping("project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        projectService.delete(id);
        return Response.success();
    }

}
