package eci.server.DashBoardModule.controller;

import eci.server.BomModule.dto.DevelopmentRequestDto;
import eci.server.BomModule.service.BomService;
import eci.server.ItemModule.dto.response.Response;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://localhost:3000")
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
