package eci.server.CRCOModule.controller.features;

import eci.server.CRCOModule.dto.featurescond.CoEffectReadCondition;
import eci.server.CRCOModule.dto.featurescond.CrReasonReadCondition;
import eci.server.CRCOModule.service.features.CoEffectService;
import eci.server.CRCOModule.service.features.CrReasonService;
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
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
public class CoEffectController{
    private final CoEffectService coEffectService;
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/coEffectId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CoEffectReadCondition cond) {
        return Response.success(
                coEffectService.
                        readAll(cond));
    }
}

