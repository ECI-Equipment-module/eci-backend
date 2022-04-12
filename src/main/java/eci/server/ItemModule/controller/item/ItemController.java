package eci.server.ItemModule.controller.item;

import eci.server.aop.AssignMemberId;
import eci.server.ItemModule.dto.item.ItemCreateRequest;
import eci.server.ItemModule.dto.item.ItemReadCondition;
import eci.server.ItemModule.dto.item.ItemUpdateRequest;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${whitelist.main}")
public class ItemController {

    private final ItemService itemService;

    /**
     * 아이템 생성
     *
     * @param req
     * @return 200 (success)
     */

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response create(
            @Valid @ModelAttribute
                    //Content-Type = multipart/form-data
                    ItemCreateRequest req
    ) {

        return Response.success(
                itemService.create(req));
    }

    /**
     * 특정 아이템 조회
     *
     * @param id
     * @return 200 (success)
     */

    @GetMapping(value = "/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                itemService.read(id)

        );
    }

    /**
     * 특정 아이템 조회
     *
     * @return 200 (success)
     */

    @GetMapping("/todos")
    @ResponseStatus(HttpStatus.OK)
    public Response todos() {
        return Response.success(
                itemService.readTodo()
        );
    }


    /**
     * 특정 아이템+딸린 썸네일 이미지 삭제
     *
     * @param id
     * @return 200 (success)
     */

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @PathVariable Long id) {

        itemService.delete(id);
        return Response.success();
    }

    /**
     * 특정 아이템 수정
     *
     * @param id
     * @param req
     * @return
     */

    @PutMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute ItemUpdateRequest req) {

        return Response.success(itemService.update(id, req));
    }

    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ItemReadCondition cond) {
        return Response.success(itemService.readAll(cond));
    }

    /**
     * 특정 사진 조회
     *
     * @return 200 (success)
     */

    @GetMapping(value=  "/image/{id}", produces= MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] img(
            @PathVariable Long id) {
        {
            byte[] image = itemService.readImg(id);
            return image;

        }
    }

}