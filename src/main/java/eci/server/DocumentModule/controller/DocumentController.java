package eci.server.DocumentModule.controller;

import eci.server.DocumentModule.dto.DocumentCreateRequest;
import eci.server.DocumentModule.dto.DocumentTempCreateRequest;
import eci.server.DocumentModule.dto.DocumentUpdateRequest;
import eci.server.DocumentModule.service.DocumentService;
import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.ProjectModule.dto.project.ProjectTempCreateUpdateResponse;
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
public class DocumentController {

    private final DocumentService documentService;


    /**
     * 아이템 개정용 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/doc/{targetId}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response docReviseCreate(
            @PathVariable Long targetId,
            @Valid @ModelAttribute
                    DocumentCreateRequest req
    ) {

        NewItemCreateResponse response =
                documentService.reviseCreate
                (req, targetId);

        return Response.success(
                response
        );
    }



    /**
     * 아이템 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/doc")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response docCreate(
            @Valid @ModelAttribute
                    DocumentCreateRequest req
    ) {

        NewItemCreateResponse response =
                documentService.create(req);



        return Response.success(
                response
        );
    }

    /**
     * 아이템 임시저장 생성
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/doc/temp/{targetId}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response docReviseTempCreate(
            @PathVariable Long targetId,
            @Valid @ModelAttribute
                    DocumentTempCreateRequest req
    ) {

        ProjectTempCreateUpdateResponse response =
                documentService.tempCreate(
                req
        );

        return Response.success(
                response
        );
    }

    /**
     * 아이템 임시저장 생성
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/doc/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response docTempCreate(
            @Valid @ModelAttribute
                    DocumentTempCreateRequest req
    ) {

        return Response.success(
                documentService.tempCreate(
                        req
                )
        );
    }


    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/doc/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response documentDelete(
            @PathVariable Long id
    ) {

        documentService.delete(id);
        return Response.success();
    }

    @AssignMemberId
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping(value = "/doc/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                documentService.read(id)

        );
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/doc/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    DocumentUpdateRequest req
    ) {

        return Response.success(
                documentService.update(id, req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/doc/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    DocumentUpdateRequest req
    ) {

        return Response.success(
                documentService.tempEnd(id, req));
    }

}
