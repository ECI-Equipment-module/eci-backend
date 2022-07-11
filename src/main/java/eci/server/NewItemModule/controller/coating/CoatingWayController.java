package eci.server.NewItemModule.controller.coating;

import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.coatingWay.CoatingWayReadCondition;
import eci.server.NewItemModule.service.coating.CoatingWayService;
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
public class CoatingWayController {
    private final CoatingWayService coatingWayService;

    @GetMapping("/coatingWayId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid CoatingWayReadCondition cond) {
        return Response.success(
                coatingWayService.
                        readAll(cond));
    }
}
