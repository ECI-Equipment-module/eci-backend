package eci.server.ItemModule.controller.item;

import eci.server.ItemModule.dto.manufacture.ManufactureReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.item.ManufactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ManufactureController {

    private final ManufactureService manufactureService;

    @GetMapping("/manufactures")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ManufactureReadCondition cond) {
        return Response.success(
                manufactureService.
                        readAll(cond));
    }

}
