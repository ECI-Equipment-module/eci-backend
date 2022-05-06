package eci.server.DashBoardModule.controller;

import eci.server.ItemModule.dto.item.ItemTemporaryCreateRequest;
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
@CrossOrigin(origins = "https://localhost:3000")

public class DashboardController {

    private final ItemService itemService;

    /**
     * 특정 아이템 조회
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/todos")
    @ResponseStatus(HttpStatus.OK)
    public Response todos() {
        return Response.success(
                itemService.readTodo()
        );
    }


}
