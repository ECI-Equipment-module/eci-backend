package eci.server.ReleaseModule.controller;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ReleaseModule.service.ReleaseOrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class ReleaseOrgController {
    private final ReleaseOrgService releaseOrgService;

    @GetMapping("/releaseOrganizationId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ProduceOrganizationReadCondition cond) {
        return Response.success(
                releaseOrgService.
                        readAll(cond));
    }
}

