package eci.server.ProjectModule.controller.projectLevel;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
import eci.server.ProjectModule.service.projectLevel.ProjectLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class ProjectLevelController {
    private final ProjectLevelService projectLevelService;

    @GetMapping("/project-level")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ProjectLevelReadCondition cond) {
        return Response.success(
                projectLevelService.
                        readAll(cond));
    }
}
