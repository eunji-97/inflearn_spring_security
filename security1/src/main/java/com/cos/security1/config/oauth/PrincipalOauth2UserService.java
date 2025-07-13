package com.cos.security1.config.oauth;


import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.RoleType;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    //구글로부터 받은 정보를 후처리하는 메소드
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("ClientRegistration :: " + userRequest.getClientRegistration()); //RegistrationID로 어떤 Oauth 로 로그인했는지 확인
        System.out.println("AccessToke :: " + userRequest.getAccessToken().getTokenValue());

        //구글 로그인 버튼 > 구글 로그인 창 > 로그인 > code 리턴 (oauth-client 라이브러리) > accesstoken 요청
        //== userRequest 정보 > loadUser > 구글로부터 회원 프로필 받음
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User :: " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("비밀번호");
        String email = oAuth2User.getAttribute("email");

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            log.info("구글 로그인이 최초");
            userEntity = userRepository.save(User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(RoleType.USER)
                    .build());
        }else {
            log.info("구글 로그인을 이미 함");
        }

        //위 정보로 회원가입을 진행할 수 있다
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
