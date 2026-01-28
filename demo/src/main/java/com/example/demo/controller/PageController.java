package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping({ "/index"})
    public String indexPage() {
        return "index";
    }

    @GetMapping("/auth")
    public String authPage() {
        return "auth";
    }

    @GetMapping("/me")
    public String mePage() {
        return "me";
    }
    @GetMapping("/history")
    public String historyPage() {
        return "history";
    }

	@GetMapping("/me/info-page")
	public String meInfoPage(){
		return "me-info";
	}

	@GetMapping("/historyChat-search")
	public String historySearch(){
		return "history-search";
	}
}
