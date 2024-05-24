package com.jaewon.oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class Oauth2Controller {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
