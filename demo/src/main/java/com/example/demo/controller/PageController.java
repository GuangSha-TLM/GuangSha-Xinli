package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    @GetMapping("/auth")
    public String authPage() {
        return "auth";
    }

    @GetMapping("/me")
    public String mePage() {
        return "me";
    }
}
