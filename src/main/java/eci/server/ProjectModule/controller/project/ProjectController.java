package eci.server.ProjectModule.controller.project;

import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.ProjectModule.dto.project.ProjectCreateRequest;
import eci.server.ProjectModule.dto.project.ProjectReadCondition;
import eci.server.ProjectModule.dto.project.ProjectTemporaryCreateRequest;
import eci.server.ProjectModule.dto.project.ProjectUpdateRequest;
import eci.server.ProjectModule.service.ProjectService;
import eci.server.aop.AssignMemberId;
import eci.server.aop.AssignModifierId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
public class ProjectController {

    private final ProjectService projectService;
    /**대쉬보드에서 읽어오는 경우
     *
     * @param cond
     * @return
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @AssignMemberId
    @GetMapping("/project")
    @ResponseStatus(HttpStatus.OK)
    public Response readDashboardAll(@Valid ProjectReadCondition cond) {
        return Response.success(
                projectService.
                        readDashboardAll(cond));
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PutMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId //수정자 추가
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProjectUpdateRequest req) {

        return Response.success(projectService.update(id, req));
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PutMapping("/project/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    ProjectUpdateRequest req
    ) {

        return Response.success(
                projectService.tempEnd(id, req)
        );
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @DeleteMapping("project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        projectService.delete(id);
        return Response.success();
    }


    @GetMapping("/project/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(projectService.read(id));
    }



}
