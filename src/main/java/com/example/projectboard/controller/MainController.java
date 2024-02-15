package com.example.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // localhost:8080/articles 로 매번 들어가는 것보다 메인에 들어가면 바로 /articles 페이지를 보여주기 위함
    @GetMapping("/")
    public String root() {
        return "forward:/articles";
    }
}
