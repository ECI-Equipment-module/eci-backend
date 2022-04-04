package eci.server.config.guard;

import eci.server.config.security.CustomAuthenticationToken;
import eci.server.config.security.CustomUserDetails;
import eci.server.entity.member.Member;
import eci.server.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthHelper {

    //액세스 토큰을 이용한 요청자의 정보만 컨텍스트에 저장, 토큰 타입 판별 이유 없음

    public boolean isAuthenticated() {
        /**
         * 요청자 인증 여부 (로그인 여부)
         */
        return getAuthentication() instanceof CustomAuthenticationToken &&
                getAuthentication().isAuthenticated();
    }

    public Long extractMemberId() {
        /**
         * 요청자 ID 추출
         */

//
//        System.out.println("AuthHelper");
//        System.out.println(getAuthentication().getPrincipal());
//        System.out.println(getAuthentication().getPrincipal().getClass().getName());
//        System.out.println(getUserDetails().getAuthorities());
//        System.out.println(getUserDetails().getUsername());
//        System.out.println("AuthHelper end");

        return Long.valueOf(getUserDetails().getUserId());
    }

    public Set<RoleType> extractMemberRoles() {
        /**
         * 요청자 RoleType 판별 (기본은 normal, 사이트 관리자 admin)
         */
        return getUserDetails().getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .map(strAuth -> RoleType.valueOf(strAuth))
                .collect(Collectors.toSet());
    }

    private CustomUserDetails getUserDetails() {
        /**
         * 사용자 정보
         */
        /*
         * SecurityContextHolder의 Authentication 정보 안의 Principal(인증정보 담는 객체) 가져오기
         */
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private Authentication getAuthentication() {
        /**
         * SecurityContext 가 담고 있는 Authentication 가져옴
         */
        /* SecurityContext : 접근 주체와 인증에 대한 정보 담는 Context
           접근 주체인 Authentication 담음

            SecurityContextHolder 가 SecurityContextHolderStrategy 를 통해
            SecurityContext 를 반환 &
            SecurityContext 가 담는
            Authentication 정보 가져옴
         */
        return SecurityContextHolder.getContext().getAuthentication();
    }

}