package eci.server.service.sign;

import eci.server.dto.sign.RefreshTokenResponse;
import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignInResponse;
import eci.server.dto.sign.SignUpRequest;
import eci.server.entity.member.Member;
import eci.server.entity.member.Role;
import eci.server.entity.member.RoleType;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import eci.server.exception.member.sign.MemberEmailAlreadyExistsException;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.exception.member.sign.PasswordNotValidateException;
import eci.server.exception.member.sign.RoleNotFoundException;
import eci.server.repository.member.MemberRepository;
import eci.server.repository.member.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    @InjectMocks
    SignService signService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenService tokenService;

    @Test
    void signUpTest() {
        // given
        SignUpRequest req = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        // when
        signService.signUp(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void validateSignUpByDuplicateEmailTest() {
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @Test
    void signUpRoleNotFoundTest() {
        // given
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void signInTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(tokenService.createAccessToken(anyString())).willReturn("access");
        given(tokenService.createRefreshToken(anyString())).willReturn("refresh");

        // when
        SignInResponse res = signService.signIn(new SignInRequest("email", "password"));

        // then
        assertThat(res.getAccessToken()).isEqualTo("access");
        assertThat(res.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(PasswordNotValidateException.class);
    }


    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("email", "password", "username", "department", "contact");
    }

    private Member createMember() {
        return new Member("email", "password", "username", "department","contact", emptyList());
    }

    /**
     * 리프레쉬 토큰 정상적인 케이스
     */
    @Test
    void refreshTokenTest() {
        // given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        given(tokenService.validateRefreshToken(refreshToken)).willReturn(true);
        given(tokenService.extractRefreshTokenSubject(refreshToken)).willReturn(subject);
        given(tokenService.createAccessToken(subject)).willReturn(accessToken);

        // when
        RefreshTokenResponse res = signService.refreshToken(refreshToken);

        // then
        assertThat(res.getAccessToken()).isEqualTo(accessToken);
    }

    /**
     * 유효하지 않은 토큰으로 인한 예외 케이스
     */
    @Test
    void refreshTokenExceptionByInvalidTokenTest() {
        // given
        String refreshToken = "refreshToken";
        given(tokenService.validateRefreshToken(refreshToken)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken))
                .isInstanceOf(AuthenticationEntryPointException.class);
    }

}