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
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
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


    @GetMapping("/route/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(
                newRouteService.read(id)
        );
    }

    /**
     * 아이템 타입 넘겨주면
     * @param id
     * @return
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
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
     * 현시점에서 거절가능한 라우트프로덕트 아이디
     * @param id (라우트오더링)
     * @return
     */
    @GetMapping("/route/reject-possible/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response possibleRejectRouteProductId(@PathVariable Long id) {
        return Response.success(
                newRouteService.rejectPossible(id)
        );
    }

}