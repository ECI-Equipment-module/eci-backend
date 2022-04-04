package eci.server.config.security;

import eci.server.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        http    //.cors().
                //and()
                .csrf().disable()
                .httpBasic().disable()
                //.cors().configurationSource(corsConfigurationSource())
                //.and()
                .formLogin().disable()
                .authorizeRequests()
                //.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//added
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sign-in", "/sign-up","/refresh-token").permitAll()
                .antMatchers(HttpMethod.GET, "**").permitAll()
                .antMatchers(HttpMethod.GET, "/test").permitAll()
                .antMatchers(HttpMethod.DELETE, "/members/{id}/**").access("@memberGuard.check(#id)")
                .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                .antMatchers(HttpMethod.POST, "/items").authenticated()
                .antMatchers(HttpMethod.PUT, "/items/{id}").access("@itemGuard.check(#id)")
                .antMatchers(HttpMethod.DELETE, "/items/{id}").access("@itemGuard.check(#id)")
                .anyRequest().hasAnyRole("ADMIN")
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()//인증되지 않은 사용자의 접근이 거부
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()//인증된 사용자가 권한 부족 등의 사유로 인해 접근이 거부
                .addFilterBefore(new JwtAuthenticationFilter(tokenService, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        http
                .httpBasic().disable() // 1
                .formLogin().disable() // 2
                .csrf().disable() // 3
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 4
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll(); // 5

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 6
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("DELETE");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}