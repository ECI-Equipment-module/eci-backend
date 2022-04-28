package eci.server.ProjectModule.controller.clientOrg;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.service.clientOrg.ClientOrganizationService;
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
public class ClientOrganizationController {
    private final ClientOrganizationService clientOrganizationService;

    @GetMapping("/client-organization")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ClientOrganizationReadCondition cond) {
        return Response.success(
                clientOrganizationService.
                        readAll(cond));
    }
}
