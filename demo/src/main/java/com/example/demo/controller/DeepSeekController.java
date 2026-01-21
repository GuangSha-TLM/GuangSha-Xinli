package com.example.demo.controller;

import com.example.demo.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.bo.DeepSeekPromptBO;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/deepseek")
public class DeepSeekController {
    @Autowired
    private DeepSeekService deepSeekService;

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody DeepSeekPromptBO promptBO) {
        return deepSeekService.streamChatWithHistory(promptBO.getMessages());
    }
}
