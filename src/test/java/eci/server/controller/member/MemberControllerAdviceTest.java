package eci.server.controller.member;

import eci.server.advice.ExceptionAdvice;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.service.member.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerAdviceTest {
    @InjectMocks
    MemberController memberController;
    @Mock
    MemberService memberService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    public void readMemberNotFoundExceptionTest() throws Exception {
        // given
        given(memberService.read(anyLong())).willThrow(MemberNotFoundException.class);

        // when, then
        mockMvc.perform(
                        get("/api/members/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void deleteMemberNotFoundExceptionTest() throws Exception{
        // given
        doThrow(MemberNotFoundException.class).when(memberService).delete(anyLong());

        // when, then
        mockMvc.perform(
                        delete("/api/members/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

}