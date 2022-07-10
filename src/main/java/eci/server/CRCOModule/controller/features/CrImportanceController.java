package eci.server.CRCOModule.controller.features;

import eci.server.CRCOModule.dto.featurescond.CrImportanceReadCondition;
import eci.server.CRCOModule.service.features.CrImportanceService;
import eci.server.ItemModule.dto.response.Response;
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
public class CrImportanceController{
    private final CrImportanceService crImportanceService;

    @GetMapping("/crImportance")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CrImportanceReadCondition cond) {
        return Response.success(
                crImportanceService.
                        readAll(cond));
    }
}


