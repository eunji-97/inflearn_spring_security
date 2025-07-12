package com.cos.security1.config.oauth;


import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //구글로부터 받은 정보를 후처리하는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("ClientRegistration :: " + userRequest.getClientRegistration()); //RegistrationID로 어떤 Oauth 로 로그인했는지 확인
        System.out.println("AccessToke :: " + userRequest.getAccessToken().getTokenValue());

        //구글 로그인 버튼 > 구글 로그인 창 > 로그인 > code 리턴 (oauth-client 라이브러리) > accesstoken 요청
        //== userRequest 정보 > loadUser > 구글로부터 회원 프로필 받음
        System.out.println("userRequest :: " + super.loadUser(userRequest).getAttributes());
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //위 정보로 회원가입을 진행할 수 있다
        return super.loadUser(userRequest);
    }
}
