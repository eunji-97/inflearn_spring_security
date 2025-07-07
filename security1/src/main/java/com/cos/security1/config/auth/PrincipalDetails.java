package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//시큐리티가 /login 주소 요청이 오면 낚아서 로그인
//로그인 진행 후 시큐리티 session에 넣는 역할 (Security ContextHolder 라는 키값에 세션 정보 저장)
//세션에 들어갈 수 있는 오브젝트 -> Authentication 객체 (안에 유저 정보가 있어야 함 -> UserDetail 객체)

public class PrincipalDetails implements UserDetails {

    private User user; //컴포지션

    public PrincipalDetails(User user){
        this.user = user;
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
}
