package eci.server.BomModule.controller;

import eci.server.BomModule.service.BomService;
import eci.server.ItemModule.dto.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app/")
public class BomController {

    private final BomService bomService;

    // 0) BOM
    @GetMapping("/bom/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getBom(@PathVariable Long id) {
        return Response.success(
                bomService.readBom(id)
        );
    }

}
