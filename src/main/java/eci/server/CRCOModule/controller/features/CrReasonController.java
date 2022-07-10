package eci.server.CRCOModule.controller.features;

import eci.server.CRCOModule.dto.featurescond.CrReasonReadCondition;
import eci.server.CRCOModule.dto.featurescond.CrSourceReadCondition;
import eci.server.CRCOModule.service.features.CrReasonService;
import eci.server.CRCOModule.service.features.CrSourceService;
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
public class CrReasonController {
    private final CrReasonService crReasonService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/crReason")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CrReasonReadCondition cond) {
        return Response.success(
                crReasonService.
                        readAll(cond));
    }
}
