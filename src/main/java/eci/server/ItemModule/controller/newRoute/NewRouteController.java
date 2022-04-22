package eci.server.ItemModule.controller.newRoute;


import eci.server.ItemModule.dto.newRoute.*;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.dto.route.RouteCreateRequest;
import eci.server.ItemModule.dto.route.RouteReadCondition;
import eci.server.ItemModule.dto.route.RouteUpdateRequest;
import eci.server.ItemModule.service.route.RouteService;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
public class NewRouteController {
    private final eci.server.ItemModule.service.newRoute.NewRouteService newRouteService;


    @GetMapping("/newRoutes")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(
            @Valid NewRouteReadCondition cond
    ) {
        return Response.success(newRouteService.readAll(cond));
    }

    @PostMapping("/newRoutes")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createRoutes(
            @Valid NewRouteCreateRequest req) {
        newRouteService.create(req);
        return Response.success();
    }
//
//    @PostMapping("/newRoutes2")
//    @ResponseStatus(HttpStatus.CREATED)
//    @AssignMemberId
//    public Response create2(
//            @Valid NewRouteCreateRequest2 req) {
//        newRouteService.create2(req);
//        return Response.success();
//    }
//
//    @PostMapping("/newRoutes4")
//    @ResponseStatus(HttpStatus.CREATED)
//    @AssignMemberId
//    public Response create4(
//            @Valid NewRouteCreateRequest4 req) {
//        newRouteService.create4(req);
//        return Response.success();
//    }

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
            @Valid @ModelAttribute NewRouteUpdateRequest req) {
        return Response.success(
                newRouteService.update(id, req));
    }

    @PutMapping("/rejectRoutes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response rejectUpdate(
            @PathVariable Long id,
            @Valid @ModelAttribute NewRouteRejectRequest req) {
        {
            return Response.success(
                    newRouteService.rejectUpdate(
                            id, req.getComment(), req.getRejectedSequence()
                    )
            );
        }
    }



}