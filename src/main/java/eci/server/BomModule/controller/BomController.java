package eci.server.BomModule.controller;

import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.service.BomService;
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
public class BomController {

    private final BomService bomService;

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/preliminary")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createPreliminary(
            @Valid JsonSaveCreateRequest req) {

        return Response.success(
                bomService.createPreliminary(req)
        );
    }
//
//
//    @GetMapping("/preliminary")
//    @ResponseStatus(HttpStatus.OK)
//    public Response readPreliminaryAll(@Valid PreliminaryBomReadCondition cond) {
//        return Response.success(bomService.readPreliminaryAll(cond));
//    }
//
//    @GetMapping("/preliminary/card/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Response deletePreliminaryCard(@PathVariable Long id) {
//        bomService.delete(id); //삭제할 카드 아이디 건네주기
//        return Response.success();
//    }

}
