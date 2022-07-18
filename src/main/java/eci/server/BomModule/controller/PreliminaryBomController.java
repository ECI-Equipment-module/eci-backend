package eci.server.BomModule.controller;

import eci.server.BomModule.dto.prelimianry.JsonSaveCreateRequest;
import eci.server.BomModule.service.BomService;
import eci.server.BomModule.service.PreliminaryBomService;
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
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
public class PreliminaryBomController {

    private final PreliminaryBomService bomService;

    // 1) Preliminary BOM
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @PostMapping("/preliminary")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createPreliminary(
            @Valid JsonSaveCreateRequest req) {

        return Response.success(
                bomService.createPreliminary(req)
        );
    }

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/preliminary/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getPreliminary(@PathVariable Long id) {
        return Response.success(
                bomService.readPreliminary(id)
        );
    }
}

