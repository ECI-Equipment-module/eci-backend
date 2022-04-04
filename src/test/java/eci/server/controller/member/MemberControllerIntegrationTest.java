package eci.server.controller.member;


import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignInResponse;
import eci.server.entity.member.Member;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.init.TestInitDB;
import eci.server.repository.member.MemberRepository;
import eci.server.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class MemberControllerIntegrationTest {
    @Autowired WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestInitDB initDB;
    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).
                apply(
                        springSecurity()
                ).build();
        initDB.initDB();
    }

    @Test
    void readTest() throws Exception {
        // given
        Member member = memberRepository.
                findByEmail(
                        initDB.getMember1Email()
                ).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        get("/members/{id}", member.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Member member = memberRepository.
                findByEmail(
                        initDB.getMember1Email()
                ).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(
                new SignInRequest(
                        initDB.getMember1Email(), initDB.getPassword()
                )
        );

        // when, then
        mockMvc.perform(
                        delete(
                                "/members/{id}",
                                member.getId()
                        ).header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(
                initDB.getMember1Email()
        ).orElseThrow(MemberNotFoundException::new);
        SignInResponse adminSignInRes =
                signService.signIn(
                        new SignInRequest(
                                initDB.getAdminEmail(),
                                initDB.getPassword()
                        )
                );

        // when, then
        mockMvc.perform(
                        delete(
                                "/members/{id}",
                                member.getId()
                        ).header("Authorization", adminSignInRes.getAccessToken())
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(
                initDB.getMember1Email()
        ).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        delete("/members/{id}", member.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Member member = memberRepository.
                findByEmail(
                        initDB.getMember1Email()
                ).orElseThrow(MemberNotFoundException::new);
        SignInResponse attackerSignInRes =
                signService.signIn(
                        new SignInRequest(
                                initDB.getMember2Email(),
                                initDB.getPassword()
                        )
                );

        // when, then
        mockMvc.perform(
                        delete(
                                "/members/{id}",
                                member.getId()).header(
                                        "Authorization",
                                attackerSignInRes.getAccessToken()
                        )
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
    void deleteAccessDeniedByRefreshTokenTest() throws Exception {
        // given
        Member member = memberRepository.
                findByEmail(initDB.getMember1Email()).
                orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(
                new SignInRequest(
                        initDB.getMember1Email(),
                        initDB.getPassword()
                )
        );

        // when, then
        mockMvc.perform(
                        delete("/members/{id}",
                                member.getId()).header(
                                        "Authorization",
                                        signInRes.getRefreshToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

}