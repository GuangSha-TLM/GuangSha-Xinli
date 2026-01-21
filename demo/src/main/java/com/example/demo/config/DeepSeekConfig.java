package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DeepSeekConfig {
    @Value("${deepseek.key}")
    private String deepSeekKey;

    @Bean
    public WebClient deepSeekWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.deepseek.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + deepSeekKey)
                .build();
    }
}
