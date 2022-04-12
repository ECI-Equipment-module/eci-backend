package eci.server.ItemModule.controller.item;

import eci.server.ItemModule.dto.material.MaterialReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.item.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "${whitelist.main}")
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping("/materials")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid MaterialReadCondition cond) {
        return Response.success(
                materialService.
                        readAll(cond));
    }

}
