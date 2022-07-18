package eci.server.CRCOModule.controller.features;

import eci.server.CRCOModule.dto.featurescond.CoStageReadCondition;
import eci.server.CRCOModule.service.features.CoStageService;
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
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
public class CoStageController{
    private final CoStageService CoStageService;
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
    @GetMapping("/coStageId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CoStageReadCondition cond) {
        return Response.success(
                CoStageService.
                        readAll(cond));
    }
}

