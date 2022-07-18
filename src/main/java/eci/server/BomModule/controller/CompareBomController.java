package eci.server.BomModule.controller;

import eci.server.BomModule.dto.compare.CompareRequestDto;
import eci.server.BomModule.service.CompareBomService;
import eci.server.ItemModule.dto.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
public class CompareBomController {

    private final CompareBomService bomService;

    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
    @GetMapping("/compare")
    @ResponseStatus(HttpStatus.OK)
    public Response getPreliminary(
            @Valid CompareRequestDto req
    ) {
        return Response.success(
                bomService.readCompare(
                        req.getCompareId(),
                        req.getAgainstId())
        );
    }

}
