package eci.server.ProjectModule.controller.carType;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.carType.CarTypeReadCondition;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.service.carType.CarTypeService;
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
public class CarTypeController {
    private final CarTypeService carTypeService;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/carType")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CarTypeReadCondition cond) {
        return Response.success(
                carTypeService.
                        readAll(cond));
    }
}