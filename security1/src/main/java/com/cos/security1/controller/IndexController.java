package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.RoleType;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@AllArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    //@AuthenticationPrincipal를 이용해 세션의 로그인 정보를 가져올 수 있다
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails uesrDetails){ //의존성 주입, 둘다 같은 결과를 가짐
        log.info("======test/login=========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //구글 로그인을 하면 캐스팅 오류남
        log.info("authentication :: " +principalDetails.getUser());
        log.info("userDetails :: " +uesrDetails.getUser());
        return "로그인 테스트 및 정보 확인";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2){ //의존성 주입
        log.info("======test/oauth/login=========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); //구글 로그인 시 오류 안남!
        log.info("authentication :: " +oAuth2User.getAttributes());
        log.info("oAuth2 ::"+ oAuth2);
        return "OAuth2User 정보 확인";
    }

    // 스프링 시큐리티는 시큐리티 세션을 둔다 이안에는 Authentication 객체만 들어갈 수 있으며 들어가게되면 세션 생성 > 즉 로그인이 된다
    // Authentication 객체안에 들어갈 수 있는 타입은 2가지. 1) UesrDetails 2) OAuth2User

    @GetMapping({"", "/"})
    public @ResponseBody String index() {
        //컨피그 설정을 해주지 않으면 suffix가 .mustache로 잡혀 index.html 가 잡히지 않는다
        // configureViewResolvers로 재설정
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails uesrDetails){ // 일반 로그인은 uesrDetails로 유저정보를 받는다!
        log.info("principal Details :: " + uesrDetails.getUser());
        return "user";
    }

    @GetMapping("/user2")
    public @ResponseBody String user2(@AuthenticationPrincipal OAuth2User oAuth2){ // 구글 로그인은 OAuth2User로 유저정보를 받는다!
        return "user";
    }
    //그럼 두개를 다 받을 수 있도록 하는 방법은? >> 두개의 클래스를 상속받은 X 라는 클래스를 새로 만들면 OK >> PrincipalDetails에 2개를 상속받게 하자

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){ //스프링 시큐리티가 먼저 가져감 secutiryConfig로 커스텀 가능
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        log.info("user : {}", user);
        user.setRole(RoleType.USER);
        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    @PostAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')") //pre는 메서드 실행 전, post는 실행 후 권한을 확인함
    //@Secured : 컨피그에서 걸지 않아도 간단히 설정 가능, @PreAuthorize : hasAuthority 여러개 설정 가능
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "data 정보";
    }


}
