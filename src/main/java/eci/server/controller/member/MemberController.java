package eci.server.controller.member;

import eci.server.dto.response.Response;
import eci.server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

<<<<<<< HEAD
    @GetMapping("/members/{id}")
=======
    @GetMapping("/api/members/{id}")
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(memberService.read(id));
    }

<<<<<<< HEAD
    @DeleteMapping("/members/{id}")
=======
    @DeleteMapping("/api/members/{id}")
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }
}