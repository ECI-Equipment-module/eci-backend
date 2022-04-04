package eci.server.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< HEAD
import eci.server.controller.sign.SignController;
import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignUpRequest;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import eci.server.exception.member.sign.MemberEmailAlreadyExistsException;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.exception.member.sign.RoleNotFoundException;
=======
import eci.server.controller.sign.ExceptionAdvice;
import eci.server.controller.sign.SignController;
import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignUpRequest;
import eci.server.exception.member.MemberEmailAlreadyExistsException;
import eci.server.exception.member.MemberNotFoundException;
import eci.server.exception.member.RoleNotFoundException;
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
import eci.server.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
<<<<<<< HEAD
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
=======
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SignControllerAdviceTest {
    @InjectMocks
    SignController signController;
    @Mock
    SignService signService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void signInLoginFailureExceptionTest() throws Exception {
        // given
        SignInRequest req = new SignInRequest("email@email.com", "123456a!");
        given(signService.signIn(any())).willThrow(MemberNotFoundException.class);
        // 얘가 왜 500을 리턴하는지 모르겠네
        // when, then
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void signInMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        //이메일과 비밀번호의 제약 조건이 지켜지지 않았기 때문에, MethodArgumentNotValidException
        SignInRequest req = new SignInRequest("email", "1234567");
        mockMvc.perform(
                        post("/api/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpMemberEmailAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("email@email.com", "123456a!", "username", "department", "01012345678");
        doThrow(MemberEmailAlreadyExistsException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }


    @Test
    void signUpRoleNotFoundExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("email@email.com", "123456a!", "username", "department","01012345678");
        doThrow(RoleNotFoundException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void signUpMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("", "", "", "", "");

        // when, then
        mockMvc.perform(
                        post("/api/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
<<<<<<< HEAD

    /**
     * 유효하지 않은 토큰 - AuthenticationEntryPointException 예외 발생
     */
    @Test
    void refreshTokenAuthenticationEntryPointException() throws Exception { // 1
        // given
        given(signService.refreshToken(anyString())).willThrow(AuthenticationEntryPointException.class);

        // when, then
        mockMvc.perform(
                        post("/api/refresh-token")
                                .header("Authorization", "refreshToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(-1001));
    }

    /**
     * 누락된 HTTP 요청 헤더로 인해 MissingRequestHeaderException 예외 발생
     */
    @Test
    void refreshTokenMissingRequestHeaderException() throws Exception { // 2
        // given, when, then
        mockMvc.perform(
                        post("/api/refresh-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(-1009));
    }
=======
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
}