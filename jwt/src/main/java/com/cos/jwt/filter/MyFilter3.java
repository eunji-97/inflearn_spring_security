package com.cos.jwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터3");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //토큰은 id, pw 가 정상적으로 들어와서 로그인 시 만들어서 줘야 함
        //이후 헤더에 포함되어 요청
        //넘어온 토큰이 유효한지 검증 필요
        if("POST".equals(req.getMethod())) {
            String hearderAuth = req.getHeader("Authorization"); //header에 넣어 요청 보내면 받을 수 있음
            System.out.println("hearderAuth :: " +hearderAuth);

            if("auth".equals(hearderAuth)){
                chain.doFilter(req, resp); //헤더 내용이 정확해야 다음 동작을 함

            }else {
                PrintWriter pw = resp.getWriter();
                pw.println("인증 안됨");
            }
        }
    }
}
