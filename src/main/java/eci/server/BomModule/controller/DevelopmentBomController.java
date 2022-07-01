package eci.server.BomModule.controller;

import eci.server.BomModule.dto.dev.DevelopmentRequestDto;
import eci.server.BomModule.service.DevelopmentBomService;
import eci.server.ItemModule.dto.response.Response;
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
public class DevelopmentBomController {

    private final DevelopmentBomService bomService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/development/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getDevelopment(@PathVariable Long id) {
        return Response.success(
                bomService.readDevelopment(id)
        );
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/development/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createTempSavedDevelopment(
            @Valid DevelopmentRequestDto req) {

        return Response.success(
                bomService.createAndDestroyTempParentChildren(req)
        );
    }

    /**
     * dev bom 의 read only = true 로 설정
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/development")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createSavedDevelopment(
            @Valid DevelopmentRequestDto req) {

        bomService.updateReadonlyTrue(req.getDevId());

        return Response.success(
                bomService.createAndDestroyTempParentChildren(req)
        );
    }



}

