package eci.server.DashBoardModule.controller;

import eci.server.DashBoardModule.service.DashboardService;
import eci.server.ItemModule.dto.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://localhost:3000")

public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * project to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/dashboard/project/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response projectTodo() {
        return Response.success(
                dashboardService.readProjectTodo()
        );
    }

    /**
     * design to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/dashboard/design/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response designTodo() {
        return Response.success(
                dashboardService.readDesignTodo()
        );
    }

    /**
     * project total status
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/dashboard/project/total")
    @ResponseStatus(HttpStatus.OK)
    public Response projectTotal() {
        return Response.success(
                dashboardService.readProjectTotal()
        );
    }


    /**
     * item to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/dashboard/item/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response itemTodo() {
        return Response.success(
                dashboardService.readItemTodo()
        );
    }


    /**
     * BOM to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/dashboard/bom/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response bomTodo() {
        return Response.success(
                dashboardService.readBomTodo()
        );
    }


}
