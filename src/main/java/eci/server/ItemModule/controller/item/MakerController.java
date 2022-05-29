package eci.server.ItemModule.controller.item;

import eci.server.ItemModule.dto.manufacture.MakerReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.item.MakerService;
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
public class MakerController {

    private final MakerService makerService;

    @GetMapping("/makers")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid MakerReadCondition cond) {
        return Response.success(
                makerService.
                        readAll(cond));
    }

}
