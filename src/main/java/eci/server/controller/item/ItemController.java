package eci.server.controller.item;

import eci.server.aop.AssignMemberId;
import eci.server.dto.item.ItemCreateRequest;
import eci.server.dto.item.ItemUpdateRequest;
import eci.server.dto.response.Response;
import eci.server.exception.member.auth.AccessExpiredException;
import eci.server.service.item.ItemService;
import eci.server.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    /**
     * 아이템 생성
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
     * @param id
     * @return 200 (success)
     */
    @GetMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {


        return Response.success(
                itemService.read(id)
        );
    }

    /**
     * 특정 아이템+딸린 썸네일 이미지 삭제
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

    @PutMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute ItemUpdateRequest req) {

        return Response.success(itemService.update(id, req));
    }

}