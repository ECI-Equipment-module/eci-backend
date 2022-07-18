package eci.server.NewItemModule.controller.coating;

import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.item.ColorService;
import eci.server.NewItemModule.dto.coatingType.CoatingTypeReadCondition;
import eci.server.NewItemModule.service.coating.CoatingTypeService;
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
public class CoatingTypeController {
    private final CoatingTypeService coatingTypeService;

    @GetMapping("/coatingTypeId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CoatingTypeReadCondition cond) {
        return Response.success(
                coatingTypeService.
                        readAll(cond));
    }
}

