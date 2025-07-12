package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//시큐리티가 /login 주소 요청이 오면 낚아서 로그인
//로그인 진행 후 시큐리티 session에 넣는 역할 (Security ContextHolder 라는 키값에 세션 정보 저장)
//세션에 들어갈 수 있는 오브젝트 -> Authentication 객체 (안에 유저 정보가 있어야 함 -> UserDetail 객체)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; //컴포지션

    public PrincipalDetails(User user){
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {  //해당 유저의 권한을 리턴
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }  // 계정 만료인지

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // 잠긴 계정인지

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // 인증 만료인지

    @Override
    public boolean isEnabled() {
        return true;
    } // 휴면 계정일 때

    @Override
    public String getName() {
        return "";
    }
}
