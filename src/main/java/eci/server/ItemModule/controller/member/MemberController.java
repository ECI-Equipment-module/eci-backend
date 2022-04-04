package eci.server.ItemModule.controller.member;

import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j

@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(memberService.read(id));
    }

    @DeleteMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }
}