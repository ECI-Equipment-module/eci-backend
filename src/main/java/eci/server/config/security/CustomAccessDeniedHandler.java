package eci.server.config.security;

import eci.server.config.guard.ItemGuard;
import eci.server.entity.member.Member;
import eci.server.repository.item.ItemRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 인증은 되었지만,
 * 사용자가 접근 권한이 없을 시 작동 핸들러
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("accessdeniedhandlerrrrrrrrrrrrrrrrr");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//

        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getAuthorities());
        System.out.println(authentication.getName());
        System.out.println("new test");

        response.sendRedirect("/exception/access-denied");
    }

}