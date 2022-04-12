package eci.server.ItemModule.controller.item;

import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.item.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://6a3e-118-36-38-193.ngrok.io")
public class ColorController {
    private final ColorService colorService;

    @GetMapping("/colors")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ColorReadCondition cond) {
        return Response.success(
                colorService.
                        readAll(cond));
    }
}
