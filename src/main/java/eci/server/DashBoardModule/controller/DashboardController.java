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
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")

public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * project to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
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
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/dashboard/bom/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response bomTodo() {
        return Response.success(
                dashboardService.readBomTodo()
        );
    }

    /**
     * CRCO to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/dashboard/crco/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response CRCOTodo() {
        return Response.success(
                dashboardService.readCrCoTodo()
        );
    }

    /**
     * RELEASE to-do api
     *
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/dashboard/release/todo")
    @ResponseStatus(HttpStatus.OK)
    public Response RELEASETodo() {
        return Response.success(
                dashboardService.readRELEASETodo()
        );
    }

}