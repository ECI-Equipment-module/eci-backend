package eci.server.service.sign;


import eci.server.dto.member.MemberDto;
import eci.server.dto.sign.RefreshTokenResponse;
import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignInResponse;
import eci.server.dto.sign.SignUpRequest;
import eci.server.entity.member.Member;
import eci.server.entity.member.RoleType;
import eci.server.exception.member.auth.AuthenticationEntryPointException;
import eci.server.exception.member.sign.MemberEmailAlreadyExistsException;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.exception.member.sign.PasswordNotValidateException;
import eci.server.exception.member.sign.RoleNotFoundException;
import eci.server.repository.member.MemberRepository;
import eci.server.repository.member.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
/**
 * 액세스 토큰 / 리프레시 토큰발급
 */
public class SignService {
    Logger logger = LoggerFactory.getLogger(SignService.class);

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        memberRepository.save(SignUpRequest.toEntity(
                req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));
    }
    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(MemberNotFoundException::new);
        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = tokenService.createAccessToken(subject);
        String refreshToken = tokenService.createRefreshToken(subject);
        MemberDto member1 =  MemberDto.toDto(memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new));
        return new SignInResponse(accessToken, refreshToken, member1);
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new PasswordNotValidateException();
        }
    }

    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }

    /**
     * 검증된 리프레시 토큰이라면 액세스 재발급해서 돌려주기
     */
    public RefreshTokenResponse refreshToken(String rToken) {
    /*
    리프레시 토큰 검증 후 리프레시 유효하면 새로운 access 돌려주기
    유효하지 않다면 validateRefreshToken 에서 에러 던짐
     */
        validateRefreshToken(rToken);
        String subject = tokenService.extractRefreshTokenSubject(rToken);
        String accessToken = tokenService.createAccessToken(subject);
        return new RefreshTokenResponse(accessToken);

    }

    /**
     * 리프레시 토큰이 유효하지 않다면 401 에러
     */
    private void validateRefreshToken(String rToken) {

        if(!tokenService.validateRefreshToken(rToken)) {
            throw new AuthenticationEntryPointException();
        }
    }


}