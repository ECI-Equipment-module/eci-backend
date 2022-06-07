package eci.server.BomModule.controller;

import eci.server.BomModule.dto.PreliminaryBomCardCreateRequest;
import eci.server.BomModule.service.BomService;
import eci.server.ItemModule.dto.response.Response;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomController {
    private final BomService bomService;

    @PostMapping("/preliminary")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createPreliminary(
            @Valid PreliminaryBomCardCreateRequest req) {

        return Response.success(
                bomService.createCard(req)
        );
    }

}
