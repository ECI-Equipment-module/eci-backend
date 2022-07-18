package eci.server.ProjectModule.controller.projectType;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
import eci.server.ProjectModule.dto.projectType.ProjectTypeReadCondition;
import eci.server.ProjectModule.service.projectLevel.ProjectLevelService;
import eci.server.ProjectModule.service.projectType.ProjectTypeService;
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
public class ProjectTypeController {
    private final ProjectTypeService projectTypeService;

    @GetMapping("/project-type")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid ProjectTypeReadCondition cond) {
        return Response.success(
                projectTypeService.
                        readAll(cond));
    }
}
