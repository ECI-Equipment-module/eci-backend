package eci.server.DesignModule.controller;

import eci.server.DesignModule.dto.DesignCreateRequest;
import eci.server.DesignModule.dto.DesignCreateUpdateResponse;
import eci.server.DesignModule.dto.DesignTempCreateRequest;
import eci.server.DesignModule.dto.DesignUpdateRequest;
import eci.server.DesignModule.service.DesignService;
import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
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
@CrossOrigin(origins = "https://localhost:3000")
public class DesignController {

    private final DesignService designService;

//    @CrossOrigin(origins = "https://localhost:3000")
//    @AssignMemberId
//    @GetMapping("/design")
//    @ResponseStatus(HttpStatus.OK)
//    public Response readDesignAll(@Valid ProjectReadCondition cond) {
//        return Response.success(
//                designService.
//                        readDashboardAll(cond));
//    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/design/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response tempCreate(
            @Valid @ModelAttribute
                    DesignTempCreateRequest req
    ) {

        return Response.success(

                designService.tempCreate(req));
    }


    /**
     * 프로젝트 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/design")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(
            @Valid @ModelAttribute
                    DesignCreateRequest req
    ) {
        DesignCreateUpdateResponse res =
                designService.create(req);

        return Response.success(res);
    }

    /**
     * 특정 프로젝트 수정
     *
     * @param id
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/design/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute DesignUpdateRequest req) {

        return Response.success(designService.update(id, req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/design/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    DesignUpdateRequest req
    ) {

        return Response.success(
                designService.tempEnd(id, req));
    }




    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("design/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        designService.delete(id);
        return Response.success();
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/design/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(designService.read(id));
    }



}
