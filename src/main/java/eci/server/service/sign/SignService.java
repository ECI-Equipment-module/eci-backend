package eci.server.service.sign;


import eci.server.dto.sign.SignInRequest;
import eci.server.dto.sign.SignInResponse;
import eci.server.dto.sign.SignUpRequest;
import eci.server.entity.member.Member;
import eci.server.entity.member.RoleType;
import eci.server.exception.member.*;
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
public class SignService {
    Logger logger = LoggerFactory.getLogger(SignService.class);

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void signUp(SignUpRequest req) {
        logger.info("i am in signupservicenow~~~~~~~~~~~~");
        validateSignUpInfo(req);
        logger.info("pss1");
        memberRepository.save(SignUpRequest.toEntity(req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));

        logger.info("pss2");
        Optional<Member> madey = memberRepository.findByEmail(req.getEmail());
        System.out.println(madey.get().getEmail());
        logger.info("pss3");
    }

    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(MemberNotFoundException::new);
        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = tokenService.createAccessToken(subject);
        String refreshToken = tokenService.createRefreshToken(subject);
        return new SignInResponse(accessToken, refreshToken);
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
}