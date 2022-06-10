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
        @GetMapping("/preliminary/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Response getPreliminary(@PathVariable Long id) {
            return Response.success(
                    bomService.readPreliminary(id)
            );
        }
}
