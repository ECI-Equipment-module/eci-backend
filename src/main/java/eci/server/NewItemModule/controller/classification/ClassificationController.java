package eci.server.NewItemModule.controller.classification;

import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.coatingType.CoatingTypeReadCondition;
import eci.server.NewItemModule.service.classification.ClassificationService;
import eci.server.NewItemModule.service.coating.CoatingTypeService;
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
public class ClassificationController {
    private final ClassificationService classificationService;

    @GetMapping("/classification1")
    @ResponseStatus(HttpStatus.OK)
    public Response readClassification1All() {
        return Response.success(
                classificationService.
                        readAllClassification1());
    }



}
