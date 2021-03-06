package eci.server.ItemModule.controller.newRoute;


import eci.server.ItemModule.dto.newRoute.routeOrdering.*;
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


    @GetMapping("/route")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(
            @Valid RouteOrderingReadCondition cond
    ) {
        return Response.success(newRouteService.readAll(cond));
    }

    @PostMapping("/route")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.createItemRoute(req)
        );
    }

    @PostMapping("/route/cr")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createCrRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.createCrRoute(req)
        );
    }


    @PostMapping("/route/co")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createCoRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.createCoRoute(req)
        );
    }

    @PostMapping("/route/release")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createReleaseRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.createReleaseRoute(req)
        );
    }

    @PostMapping("/route/doc")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response createDocRoutes(
            @Valid RouteOrderingCreateRequest req) {

        return Response.success(
                newRouteService.createDocRoute(req)
        );
    }


    @GetMapping("/route/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(
                newRouteService.read(id)
        );
    }

    /**
     * ????????? ?????? ????????????
     * @param id
     * @return
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/routeByItem/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response readRouteByItem(@PathVariable Long id) {
        return Response.success(
                newRouteService.readRouteByItem(id)
        );
    }

    @PutMapping("/approveRoute/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute RouteOrderingUpdateRequest req) {

        RouteUpdateResponse routeUpdateResponse = newRouteService.approveUpdate(id, req);

        return Response.success(
                routeUpdateResponse
        );
    }

    @PutMapping("/rejectRoute/{id}")
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

    /**
     * ??????????????? ??????????????? ????????????????????? ?????????
     * @param id (??????????????????)
     * @return
     */
    @GetMapping("/route/reject-possible/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response possibleRejectRouteProductId(@PathVariable Long id) {
        return Response.success(
                newRouteService.rejectPossible(id)
        );
    }

    @GetMapping("/route/members/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public Response membersFromBeforeRoute(@PathVariable Long routeId) {
        return Response.success(
                newRouteService.memberRead(routeId)
        );
    }


}