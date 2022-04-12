package eci.server.ItemModule.controller.member;

import eci.server.ItemModule.dto.member.MemberReadCondition;
import eci.server.ItemModule.dto.response.Response;
import eci.server.ItemModule.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j

@CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
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

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid MemberReadCondition cond) {
        return Response.success(memberService.readAll(cond));
    }
}