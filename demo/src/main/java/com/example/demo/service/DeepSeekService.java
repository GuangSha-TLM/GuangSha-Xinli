package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DeepSeekService {
        private static final Logger log = LoggerFactory.getLogger(DeepSeekService.class);
    @Autowired
    private WebClient deepSeekWebClient;

    public Flux<String> streamChat(String prompt) {
        Map<String, Object> requestBody = Map.of(
            "model", "deepseek-chat",
            "messages", new Object[]{Map.of("role", "user", "content", prompt)},
            "stream", true
        );
        return deepSeekWebClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(String.class)
            .doOnNext(data -> log.info("DeepSeek返回: {}", data));
    }
}
