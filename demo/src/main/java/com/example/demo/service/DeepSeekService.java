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
import com.example.demo.constants.DeepSeekPrompts;
import com.example.demo.domain.bo.DeepSeekMessageBO;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeepSeekService {


        private static final Logger log = LoggerFactory.getLogger(DeepSeekService.class);

    @Autowired
    private WebClient deepSeekWebClient;


                public Flux<String> streamChatWithHistory(List<DeepSeekMessageBO> history) {
                    String sysPrompt = DeepSeekPrompts.SYSTEM_PROMPT.replace("{studentName}", "the student");
                    List<Map<String, String>> messages = new ArrayList<>();
                    messages.add(java.util.Map.of("role", "system", "content", sysPrompt));
                    if (history != null) {
                        for (DeepSeekMessageBO msg : history) {
                            String role = msg.getRole();
                            // 只允许 user/assistant
                            if (!"user".equals(role) && !"assistant".equals(role)) {
                                role = "user";
                            }
                            messages.add(java.util.Map.of("role", role, "content", msg.getContent()));
                        }
                    }
                    Map<String, Object> requestBody = java.util.Map.of(
                            "model", "deepseek-chat",
                            "messages", messages,
                            "stream", true
                    );
                    log.info("DeepSeek请求体: {}", requestBody);
                    return deepSeekWebClient.post()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(requestBody))
                            .accept(MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToFlux(String.class)
                            .doOnNext(data -> log.info("DeepSeek返回: {}", data));
                }
}
