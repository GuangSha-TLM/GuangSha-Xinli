package com.example.demo.controller;

import com.example.demo.config.SessionHolder;
import com.example.demo.domain.dto.User;
import com.example.demo.domain.entity.ResponseCode;
import com.example.demo.service.DeepSeekService;
import com.example.demo.service.AIChatMessageService;
import com.example.demo.domain.dto.AiChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.bo.DeepSeekPromptBO;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deepseek")
public class DeepSeekController {
    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private AIChatMessageService aiChatMessageService;

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody DeepSeekPromptBO promptBO) {

        return deepSeekService.streamChatWithHistory(promptBO.getMessages());
    }

    @PostMapping("/saveChatHistory")
    public void saveChatHistory(@RequestBody DeepSeekPromptBO promptBO) {
        String transactionId = promptBO.getTransactionId();
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        AiChatMessage chatMessage = aiChatMessageService.lambdaQuery()
                .eq(AiChatMessage::getTransactionId, transactionId)
                .one();
        java.util.List<com.example.demo.domain.bo.DeepSeekMessageBO> allMessages = new java.util.ArrayList<>();
        if (chatMessage != null) {
            // 已有记录，取出历史消息
            try {
                java.util.List<com.example.demo.domain.bo.DeepSeekMessageBO> oldMessages = mapper.readValue(chatMessage.getMessagesJson(),
                        mapper.getTypeFactory().constructCollectionType(java.util.List.class, com.example.demo.domain.bo.DeepSeekMessageBO.class));
                allMessages.addAll(oldMessages);
            } catch (Exception e) {
                // ignore parse error
            }
        } else {
            // 新建
            chatMessage = new AiChatMessage();
            chatMessage.setTransactionId(transactionId);
        }
        // 只追加本次新消息（去掉历史重复）
        java.util.List<com.example.demo.domain.bo.DeepSeekMessageBO> newMessages = promptBO.getMessages();
        if (newMessages != null && !newMessages.isEmpty()) {
            int oldSize = allMessages.size();
            if (newMessages.size() > oldSize) {
                allMessages.addAll(newMessages.subList(oldSize, newMessages.size()));
            }
        }
        try {
            String messagesJson = mapper.writeValueAsString(allMessages);
            chatMessage.setMessagesJson(messagesJson);
        } catch (Exception e) {
            chatMessage.setMessagesJson("[]");
        }
        aiChatMessageService.saveOrUpdate(chatMessage);
    }

	@PostMapping("/history/data")
	public ResponseCode<List<AiChatMessage>> findMsgByDate(HttpSession session){
		User loginUser = SessionHolder.getSession(session);
		// getId()取出Session里装的用户id
		Long user = loginUser.getId();
		return ResponseCode.buildResponse(aiChatMessageService.findByDateRange(user));
	}
}
