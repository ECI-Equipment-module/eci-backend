package eci.server.ProjectModule.controller.project;

import eci.server.DesignModule.dto.DesignCreateUpdateResponse;
import eci.server.ItemModule.dto.item.ItemUpdateResponse;
import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.ProjectModule.dto.project.*;
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

    ////0712

    /**
     * revise 로 새로 만들어진 아이템으로 프로젝트 재연결
     * @param newMadeItemId
     * @param revisedId
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PutMapping("/project/{revisedId}/{newMadeItemId}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId //수정자 추가
    public Response reviseUpdate(
            @PathVariable Long newMadeItemId,//바꿔치기 할 아이템 아이디
            @PathVariable Long revisedId, //기존 프로젝트 아이디
            @Valid @ModelAttribute ProjectUpdateRequest req) {
        NewItemCreateResponse response1 =
                projectService.changeProjectItemToNewMadeItem(revisedId, newMadeItemId);
        ItemUpdateResponse res2 = projectService.update2(revisedId, newMadeItemId);
        System.out.println(response1 + "이 수행1111 ");
        ItemUpdateResponse response = projectService.update(revisedId, req);
        //기존 프로젝트 업데이트

        NewItemCreateResponse response2 =
                projectService.changeProjectItemToNewMadeItem(revisedId, newMadeItemId);
        projectService.update2(revisedId, newMadeItemId);
        System.out.println(response1 + "이 수행222 ");

        // 기존 프로젝트 아이템 값만 revisedId 라는 새 아이템으로 바꿔치기 해주기
        //response.setRouteId(projectService.routeIdReturn(newMadeItemId));
        return Response.success(
                response
        );
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PutMapping("/project/temp/end/{revisedId}/{newMadeItemId}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response reviseTempEnd(
            @PathVariable Long newMadeItemId, //바꿔치기 할 아이템 아이디
            @PathVariable Long revisedId, //기존 프로젝트 아이디
            @Valid @ModelAttribute
                    ProjectUpdateRequest req
    ) {
        NewItemCreateResponse response1 =
                projectService.changeProjectItemToNewMadeItem(revisedId, newMadeItemId);
        ItemUpdateResponse res2 = projectService.update2(revisedId, newMadeItemId);
        ItemUpdateResponse response = projectService.tempEnd(revisedId, req);
        // 기존 프로젝트 업데이트
        projectService.changeProjectItemToNewMadeItem(revisedId, newMadeItemId);
        // 기존 프로젝트 아이템 값만 new item 라는 새 아이템으로 바꿔치기 해주기
        NewItemCreateResponse response3 =
                projectService.changeProjectItemToNewMadeItem(revisedId, newMadeItemId);
        ItemUpdateResponse res4 = projectService.update2(revisedId, newMadeItemId);
        response.setRouteId(projectService.routeIdReturn(newMadeItemId));
        System.out.println(response1 + "이 수행222 ");
        return Response.success(
                response
        );
    }

    ///0712

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
