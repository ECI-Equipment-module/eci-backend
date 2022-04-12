package eci.server.ItemModule.controller.route;

import eci.server.aop.AssignMemberId;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.dto.route.RouteCreateRequest;
import eci.server.ItemModule.dto.route.RouteReadCondition;
import eci.server.ItemModule.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RouteController {
    private final RouteService RouteService;

    @GetMapping("/routes")
    @ResponseStatus(HttpStatus.OK)

    public Response readAll(@Valid RouteReadCondition cond) {
        return Response.success(RouteService.readAll(cond));
    }

    @PostMapping("/routes")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid RouteCreateRequest req) {
        RouteService.create(req);
        return Response.success();
    }

    @DeleteMapping("/routes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        RouteService.delete(id);
        return Response.success();
    }
}

