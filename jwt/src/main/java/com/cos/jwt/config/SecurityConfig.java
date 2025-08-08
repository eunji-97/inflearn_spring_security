package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
//    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationConfiguration authConfig) throws Exception {

        AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();

        http
                .csrf(AbstractHttpConfigurer::disable) //CSRF 보호해제
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않겠다
                )
                .addFilter(corsFilter) //시큐리티 필터에 등록
//                .addFilter(myFilter1) //그냥 필터는 오류남 Consider using addFilterBefore or addFilterAfter instead.
                .addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager)) //이거 달면 AuthenticationManager를 파라미터로 넘겨야 함
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/user/**").hasAnyAuthority("USER", "ADMIN", "MANAGER")
                        .requestMatchers("/api/v1/manager/**").hasAuthority("MANAGER")
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable); //
        //basic 방식 : ID, PW를 들고 요청 // bearer 방식 : token을 들고 요청


        return http.build();
    }



}
