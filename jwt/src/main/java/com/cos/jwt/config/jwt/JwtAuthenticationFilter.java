package com.cos.jwt.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//스프링 시큐리티에서 제공하는 UsernamePasswordAuthenticationFilter 가  있음
//login을 요청해서 username, password 를 post 전송하면 동작함
//formlogin disabled해서 동작을 안할거임 그래서 동작을 하게끔 만들어줘야 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청 시 로그인 시도를 위해 실행됨
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("login 시도");
        //1.  유저네임, 비밀번호 받기

        //2. 정상적인 정보인지 확인
        //authenticationManager로 로그인 시도 시 PrincipalDetailsService가 호출 >> loadUserByUsername이 자동 호출
        //3. PrincipalDetails를 받아 세션에 담고 (권한 관리를 위함)

        //4. JWT 토큰을 만들어 응답
        return super.attemptAuthentication(request, response);
    }
}
