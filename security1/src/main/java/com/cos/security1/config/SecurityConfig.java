package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) //@Secured 허용, @PreAuthorize/@PostAuthorize 허용
public class SecurityConfig {

    //WebSecurityConfigurerAdapter 제거되어 SecurityFilterChain으로 대체

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //CSRF 보호해제
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyAuthority("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") //hasRole는 ROLE_ 접두사를 붙임
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login //로그인 페이지 설정
                        .loginPage("/loginForm") //권한이 없으면 이 페이지로 돌아간다!
                        .loginProcessingUrl("/login") //주소 호출 시 시큐리티가 대신 로그인을 진행함
                        .defaultSuccessUrl("/")

                )
                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/loginForm")
                        // 구글 로그인 후 후처리 필요
                );

        //hasRole, hasAnyRole은 내부적으로 ROLE_ 접두어를 붙인다
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
