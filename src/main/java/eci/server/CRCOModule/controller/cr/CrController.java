package eci.server.CRCOModule.controller.cr;

import eci.server.CRCOModule.dto.cr.CrCreateRequest;
import eci.server.CRCOModule.dto.cr.CrTempCreateRequest;
import eci.server.CRCOModule.dto.cr.CrUpdateRequest;
import eci.server.CRCOModule.service.cr.CrService;
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
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
public class CrController {
    private final CrService crService;

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping(value = "/cr/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                crService.read(id)

        );
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @PutMapping("/cr/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    CrUpdateRequest req
    ) {

        return Response.success(
                crService.update(id, req));
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @PutMapping("/cr/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    CrUpdateRequest req
    ) {

        return Response.success(
                crService.tempEnd(id, req));
    }

}
