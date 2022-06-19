package eci.server.ProjectModule.controller.produceOrg;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ProjectModule.service.produceOrg.ProduceOrganizationService;
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
public class ProduceOrganizationController {
    private final ProduceOrganizationService produceOrganizationService;

    @GetMapping("/supplier")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ProduceOrganizationReadCondition cond) {
        return Response.success(
                produceOrganizationService.
                        readAll(cond));
    }
}
