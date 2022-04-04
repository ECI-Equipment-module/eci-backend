package eci.server.controller.member;

import eci.server.service.member.MemberService;
<<<<<<< HEAD

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
=======
<<<<<<< HEAD

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
=======
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
>>>>>>> c70f16eab3f2a48a8503ae9c3994e85054cfafb6
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
<<<<<<< HEAD
public class MemberControllerTest {
=======
<<<<<<< HEAD
public class MemberControllerTest {
=======
class MemberControllerTest {
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
>>>>>>> c70f16eab3f2a48a8503ae9c3994e85054cfafb6
    @InjectMocks
    MemberController memberController;
    @Mock
    MemberService memberService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
<<<<<<< HEAD
    public void readTest() throws Exception {
=======
<<<<<<< HEAD
    public void readTest() throws Exception {
=======
    void readTest() throws Exception {
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
>>>>>>> c70f16eab3f2a48a8503ae9c3994e85054cfafb6
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                        get("/api/members/{id}", id))
                .andExpect(status().isOk());
        verify(memberService).read(id);
    }

    @Test
<<<<<<< HEAD
    public void deleteTest() throws Exception {
=======
<<<<<<< HEAD
    public void deleteTest() throws Exception {
=======
    void deleteTest() throws Exception {
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
>>>>>>> c70f16eab3f2a48a8503ae9c3994e85054cfafb6
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                        delete("/api/members/{id}", id))
                .andExpect(status().isOk());
        verify(memberService).delete(id);
    }

}