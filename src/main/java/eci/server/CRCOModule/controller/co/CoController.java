package eci.server.CRCOModule.controller.co;

import eci.server.CRCOModule.dto.co.CoCreateRequest;
import eci.server.CRCOModule.dto.co.CoTempCreateRequest;
import eci.server.CRCOModule.dto.co.CoUpdateRequest;
import eci.server.CRCOModule.service.co.CoService;
import eci.server.ItemModule.dto.response.Response;
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
public class CoController {
    private final CoService coService;


    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/co/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response coTmpCreate(
            @Valid @ModelAttribute
                    CoTempCreateRequest req
    ) {

        return Response.success(
                coService.tempCreate(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/co")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response coCreate(
            @Valid @ModelAttribute
                    CoCreateRequest req
    ) {

        return Response.success(
                coService.create(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/co/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response coDelete(
            @PathVariable Long id
    ) {

        coService.delete(id);
        return Response.success();
    }

    @AssignMemberId
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping(value = "/co/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                coService.read(id)

        );
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/co/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    CoUpdateRequest req
    ) {

        return Response.success(
                coService.update(id, req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/co/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    CoUpdateRequest req
    ) {

        return Response.success(
                coService.tempEnd(id, req));
    }


}
