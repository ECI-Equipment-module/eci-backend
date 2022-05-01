package eci.server.ItemModule.controller.newRoute;


import eci.server.ItemModule.dto.newRoute.*;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.newRoute.RouteOrderingService;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class RouteOrderingController {
    private final RouteOrderingService newRouteService;


    @GetMapping("/newRoutes")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(
            @Valid RouteOrderingReadCondition cond
    ) {
        return Response.success(newRouteService.readAll(cond));
    }

    @PostMapping("/newRoutes")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.create(req)
        );
    }

    @GetMapping("/newRoutes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(
                newRouteService.read(id)
        );
    }

    @PutMapping("/approveRoutes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute RouteOrderingUpdateRequest req) {
        return Response.success(
                newRouteService.update(id, req));
    }

    @PutMapping("/rejectRoutes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response rejectUpdate(
            @PathVariable Long id,
            @Valid @ModelAttribute RouteOrderingRejectRequest req) {
        {
            return Response.success(
                    newRouteService.rejectUpdate(
                            id, req.getComment(), req.getRejectedSequence()
                    )
            );
        }
    }



}