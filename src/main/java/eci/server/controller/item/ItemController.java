package eci.server.controller.item;

import eci.server.aop.AssignMemberId;
import eci.server.dto.item.ItemCreateRequest;
import eci.server.dto.response.Response;
import eci.server.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService ItemService;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response create(
            @Valid @ModelAttribute
                    //Content-Type = multipart/form-data
                    ItemCreateRequest req
    ) {

        return Response.success(
                ItemService.create(req));
    }
}