package com.cos.jwt.controller;

import com.cos.jwt.model.Users;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }

    @GetMapping("user")
    public String user(Authentication authentication){
        return authentication.getName();
    }

    @GetMapping("reports")
    public String reports(){
        return "reports";
    }

    @GetMapping("users")
    public List<Users> users(){
        return userRepository.findAll();
    }

    @PostMapping("join")
    public String join(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("USER");
        userRepository.save(user);

        return "회원가입!";
    }

}
