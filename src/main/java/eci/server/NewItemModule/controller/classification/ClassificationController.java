package eci.server.NewItemModule.controller.classification;

import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.service.classification.ClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class ClassificationController {
    private final ClassificationService classificationService;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/classification1")
    @ResponseStatus(HttpStatus.OK)
    public Response readClassification1All() {
        return Response.success(
                classificationService.
                        readAllClassification1());
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping(value = "/attributes/{c1}/{c2}/{c3}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long c1,
            @PathVariable Long c2,
            @PathVariable Long c3
            ) {
        return Response.success(
                classificationService
                        .retrieveAttributes(c1,c2,c3)

        );
    }

}
