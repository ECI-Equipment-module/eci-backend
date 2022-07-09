package eci.server.CRCOModule.controller.cr;

import eci.server.CRCOModule.dto.CrCreateRequest;
import eci.server.CRCOModule.dto.CrReadCondition;
import eci.server.CRCOModule.dto.CrTempCreateRequest;
import eci.server.CRCOModule.service.cr.CrService;
import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import eci.server.NewItemModule.dto.newItem.create.NewItemTemporaryCreateRequest;
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
public class CrController {
    private final CrService crService;

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/cr/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response crCreate(
            @Valid @ModelAttribute
                    CrTempCreateRequest req
    ) {

        return Response.success(
                crService.tempCreate(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/cr")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response crCreate(
            @Valid @ModelAttribute
                    CrCreateRequest req
    ) {

        return Response.success(
                crService.create(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/cr/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response crDelete(
            @PathVariable Long id
    ) {

        crService.delete(id);
        return Response.success();
    }

    @AssignMemberId
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping(value = "/cr/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                crService.read(id)

        );
    }


}
