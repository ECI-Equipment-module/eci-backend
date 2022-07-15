package eci.server.config.security;

import eci.server.ItemModule.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 로그인, 회원가입은 누구나
         * 회원정보 가져오는 것은 누구나
         * 멤버 삭제는 관리자 혹은 해당 멤버만
         */
        http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests()

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sign-in", "/sign-up", "/refresh-token").permitAll()

                .antMatchers(HttpMethod.GET, "/test").permitAll()
                .antMatchers(HttpMethod.GET, "/route/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/members/{id}/**").access("@memberGuard.check(#id)")
                .antMatchers(HttpMethod.GET, "/image/**").permitAll()

                .antMatchers(HttpMethod.POST, "/items").authenticated()
                .antMatchers(HttpMethod.POST, "/items/temp").authenticated()
                .antMatchers(HttpMethod.PUT, "/items/{id}").access("@itemGuard.check(#id)")
                .antMatchers(HttpMethod.DELETE, "/items/{id}").access("@itemGuard.check(#id)")


                .antMatchers(HttpMethod.POST, "/route").authenticated()

                .antMatchers(HttpMethod.POST, "/route/project").authenticated()

                .antMatchers(HttpMethod.PUT, "/route/{id}").access("@routeGuard.check(#id)")
                .antMatchers(HttpMethod.DELETE, "/route/{id}").access("@routeGuard.check(#id)")


                .antMatchers(HttpMethod.PUT, "/approveRoute/{id}").access("@newRouteGuard.check(#id)")
                .antMatchers(HttpMethod.PUT, "/rejectRoute/{id}").access("@newRouteGuard.check(#id)")

                .antMatchers(HttpMethod.POST, "/project").authenticated()
                .antMatchers(HttpMethod.POST, "/project/temp").authenticated()
                .antMatchers(HttpMethod.PUT, "/project/{id}").access("@projectGuard.check(#id)")
                .antMatchers(HttpMethod.DELETE, "/project/{id}").access("@projectGuard.check(#id)")
                .antMatchers(HttpMethod.GET, "/project").authenticated()
                .antMatchers(HttpMethod.GET, "/project/page").authenticated()

                .antMatchers(HttpMethod.POST, "/design").authenticated()
                .antMatchers(HttpMethod.POST, "/design/temp").authenticated()
                .antMatchers(HttpMethod.PUT, "/design/{id}").access("@designGuard.check(#id)")

                .antMatchers(HttpMethod.GET, "/dashboard/design/todo").authenticated()

                .antMatchers(HttpMethod.GET, "/dashboard/project/page").authenticated()
                .antMatchers(HttpMethod.GET, "/dashboard/project/todo").authenticated()
                .antMatchers(HttpMethod.GET, "/dashboard/project/total").authenticated()

                .antMatchers(HttpMethod.POST, "/design-file/**").authenticated()
                .antMatchers(HttpMethod.POST,"/webjars/**").permitAll()

                .antMatchers(HttpMethod.GET, "/item-candidates").authenticated()
                .antMatchers(HttpMethod.POST, "/item").authenticated()
                .antMatchers(HttpMethod.POST, "/item/temp").authenticated()
                .antMatchers(HttpMethod.POST, "/item/{id}").authenticated()
                .antMatchers(HttpMethod.POST, "/item/temp/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/item/{id}").access("@newItemGuard.check(#id)")
                .antMatchers(HttpMethod.DELETE, "/item/{id}").access("@newItemGuard.check(#id)")
                .antMatchers(HttpMethod.PUT, "/item/temp/end/{id}").access("@newItemGuard.check(#id)")

                .antMatchers(HttpMethod.POST, "/preliminary").authenticated()
                .antMatchers(HttpMethod.PUT, "/preliminary").authenticated()
                .antMatchers(HttpMethod.PUT, "/preliminary/temp").authenticated()
                .antMatchers(HttpMethod.POST, "/preliminary/temp").authenticated()
                .antMatchers(HttpMethod.PUT, "/preliminary/temp/end/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/preliminary/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/preliminary").authenticated()


                .antMatchers(HttpMethod.POST, "/development").authenticated()
                .antMatchers(HttpMethod.PUT, "/development").authenticated()
                .antMatchers(HttpMethod.POST, "/development/temp").authenticated()
                .antMatchers(HttpMethod.GET, "/development/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/development").authenticated()

                .antMatchers(HttpMethod.POST, "/bom").authenticated()
                .antMatchers(HttpMethod.PUT, "/bom/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/bom/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/bom").authenticated()

                .antMatchers(HttpMethod.POST, "/dev").authenticated()
                .antMatchers(HttpMethod.PUT, "/dev/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/dev/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/dev").authenticated()

                .antMatchers(HttpMethod.POST, "/compare").authenticated()
                .antMatchers(HttpMethod.PUT, "/compare/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "compare/bom/items/page").authenticated()
                .antMatchers(HttpMethod.DELETE, "/compare").authenticated()



                .antMatchers(HttpMethod.PUT, "/project/temp/end/{id}").access("@projectGuard.check(#id)")
                .antMatchers(HttpMethod.PUT, "/project/{revisedId}/{newMadeItemId}").access("@projectGuard.check(#revisedId)")
                .antMatchers(HttpMethod.PUT, "/project/temp/end/{revisedId}/{newMadeItemId}").access("@projectGuard.check(#revisedId)")


                .antMatchers(HttpMethod.PUT, "/design/temp/end/{id}").access("@designGuard.check(#id)")


                .antMatchers(HttpMethod.GET, "/**").permitAll()//위에 명시된 get 말고는 다 허용, 맨 밑으로 위치 변경

                .antMatchers(HttpMethod.POST, "/cr").authenticated()
                .antMatchers(HttpMethod.POST, "/cr/temp").authenticated()
                .antMatchers(HttpMethod.GET, "cr/page").authenticated()
                .antMatchers(HttpMethod.DELETE, "/cr").authenticated()
                .antMatchers(HttpMethod.POST, "/route/cr").authenticated()
                .antMatchers(HttpMethod.PUT, "/cr/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/cr/temp/end/{id}").authenticated()

                .antMatchers(HttpMethod.POST, "/co").authenticated()
                .antMatchers(HttpMethod.POST, "/co/temp").authenticated()
                .antMatchers(HttpMethod.PUT, "/co/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/co/temp/end/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "co/page").authenticated()
                .antMatchers(HttpMethod.DELETE, "/co").authenticated()
                .antMatchers(HttpMethod.POST, "/route/co").authenticated()

                .antMatchers(HttpMethod.POST, "/release").authenticated()
                .antMatchers(HttpMethod.POST, "/release/temp").authenticated()
                .antMatchers(HttpMethod.PUT, "/release/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/release/temp/end/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/release").authenticated()
                .antMatchers(HttpMethod.POST, "/route/release").authenticated()

                .anyRequest().hasAnyRole("ADMIN")//멤버의 역할이 관리자인 경우에는 모든 것을 허용

                .and()
                //컨트롤러 계층 도달 전 수행
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()//인증되지 않은 사용자의 접근이 거부
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()//인증된 사용자가 권한 부족 등의 사유로 인해 접근이 거부
                //UsernamePasswordAuthenticationFilter 필터 이전에 JwtAuthentication 필터를 적용해라
                .addFilterBefore(new JwtAuthenticationFilter(tokenService, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    }
