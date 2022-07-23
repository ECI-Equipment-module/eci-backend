package eci.server.ReleaseModule.controller;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ReleaseModule.dto.ReleaseCreateRequest;
import eci.server.ReleaseModule.dto.ReleaseTempCreateRequest;
import eci.server.ReleaseModule.dto.ReleaseUpdateRequest;
import eci.server.ReleaseModule.service.ReleaseService;
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
public class ReleaseController {
    
    private final ReleaseService releaseService;


    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/release/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response coTmpCreate(
            @Valid @ModelAttribute
                    ReleaseTempCreateRequest req
    ) {

        return Response.success(
                releaseService.tempCreate(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/release")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response releaseCreate(
            @Valid @ModelAttribute
                    ReleaseCreateRequest req
    ) {

        return Response.success(
                releaseService.create(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/release/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response releaseDelete(
            @PathVariable Long id
    ) {

        releaseService.delete(id);
        return Response.success();
    }

    @AssignMemberId
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping(value = "/release/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                releaseService.read(id)

        );
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/release/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    ReleaseUpdateRequest req
    ) {

        return Response.success(
                releaseService.update(id, req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/release/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    ReleaseUpdateRequest req
    ) {

        return Response.success(
                releaseService.tempEnd(id, req));
    }



}
