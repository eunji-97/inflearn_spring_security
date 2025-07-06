package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        //컨피그 설정을 해주지 않으면 suffix가 .mustache로 잡혀 index.html 가 잡히지 않는다
        // configureViewResolvers로 재설정
        return "index";
    }
}
