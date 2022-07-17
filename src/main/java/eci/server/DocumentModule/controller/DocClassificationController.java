package eci.server.DocumentModule.controller;

import eci.server.DocumentModule.service.DocClassificationService;
import eci.server.ItemModule.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class DocClassificationController {
    private final DocClassificationService docClassificationService;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/doc/classification1")
    @ResponseStatus(HttpStatus.OK)
    public Response readDocClassification1All() {
        return Response.success(
                docClassificationService.
                        readAllDocClassification1()
        );
    }

}
