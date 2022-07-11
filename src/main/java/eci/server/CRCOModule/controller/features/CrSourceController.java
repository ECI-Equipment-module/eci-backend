package eci.server.CRCOModule.controller.features;

import eci.server.CRCOModule.dto.featurescond.CrSourceReadCondition;
import eci.server.CRCOModule.service.features.CrSourceService;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
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
public class CrSourceController {
    private final CrSourceService crSourceService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/crSourceId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CrSourceReadCondition cond) {
        return Response.success(
                crSourceService.
                        readAll(cond));
    }
}
