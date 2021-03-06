package eci.server.CRCOModule.controller.features;

import eci.server.CRCOModule.dto.featurescond.ChangedFeatureReadCondition;
import eci.server.CRCOModule.service.features.ChangedFeatureService;
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
public class ChangedFeatureController{
    private final ChangedFeatureService changedFeatureService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/changedFeatureId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ChangedFeatureReadCondition cond) {
        return Response.success(
                changedFeatureService.
                        readAll(cond));
    }
}

