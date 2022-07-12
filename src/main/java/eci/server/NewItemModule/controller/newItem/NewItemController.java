package eci.server.NewItemModule.controller.newItem;

import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateRequest;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.dto.newItem.create.NewItemTemporaryCreateRequest;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.aop.AssignMemberId;
import eci.server.ItemModule.dto.response.Response;
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

public class NewItemController {

    private final NewItemService newItemService;

    /**
     * 아이템 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PostMapping("/item/{targetId}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response reviseCreate(
            @PathVariable Long targetId,
            @Valid @ModelAttribute
                    //Content-Type = multipart/form-data
                    NewItemCreateRequest req
    ) {


        NewItemCreateResponse response = newItemService.create(req);
        newItemService.registerTargetReviseItem(targetId, response.getId());

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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PostMapping("/item/temp/{targetId}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response reviseTempCreate(
            @PathVariable Long targetId,
            @Valid @ModelAttribute
                    NewItemTemporaryCreateRequest req
    ) {

        NewItemCreateResponse response = newItemService.tempCreate(req);
        newItemService.registerTargetReviseItem(targetId, response.getId());

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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PostMapping("/item/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response tempCreate(
            @Valid @ModelAttribute
                    NewItemTemporaryCreateRequest req
    ) {

        return Response.success(
                newItemService.tempCreate(req));
    }


    //06-05 임시저장 the end 컨트롤러 (임시저장 된 것을 찐 저장 시)
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PutMapping("/item/temp/end/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignModifierId //0605 : 수정 시에는 글쓴이 아디 주입 아니고, 수정자 아이디 주입
    public Response tempEnd(
            @PathVariable Long id,
            @Valid @ModelAttribute
                    NewItemUpdateRequest req
    ) {

        return Response.success(
                newItemService.tempEnd(id, req));
    }

    /**
     * 아이템 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PostMapping("/item")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response create(
            @Valid @ModelAttribute
                    //Content-Type = multipart/form-data
                    NewItemCreateRequest req
    ) {


        return Response.success(
                newItemService.create(req));
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @GetMapping("/item")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid NewItemReadCondition cond) {
        return Response.success(
                newItemService.
                        readAll(cond));
    }

    /**
     * 특정 아이템 조회
     *
     * @param id
     * @return 200 (success)
     */
    @AssignMemberId
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @GetMapping(value = "/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                newItemService.read(id)

        );
    }



    /**
     * 특정 아이템+딸린 썸네일 이미지 삭제
     *
     * @param id
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @DeleteMapping("/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @PathVariable Long id) {

        newItemService.delete(id);
        return Response.success();
    }

    /**
     * 특정 아이템 수정
     *
     * @param id
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @PutMapping("/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute NewItemUpdateRequest req) {


        return Response.success(newItemService.update(id, req));
    }

//    @GetMapping("/children/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Response getDev(@PathVariable Long id) {
//        return Response.success(
//                newItemService.readChildAll(id)
//        );
//    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @GetMapping("/development/bom/item")
    @ResponseStatus(HttpStatus.OK)
    public Response readDevBomItem(@Valid NewItemReadCondition cond) {
        return Response.success(
                newItemService.
                        readAll(cond));
    }

}
