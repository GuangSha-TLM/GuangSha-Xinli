package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.AIChatMessageService;
import com.example.demo.domain.dto.AiChatMessage;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChatController {
    @GetMapping({"/", "/chat"})
    public String chatPage() {
        return "chat";
    }

    /**
     * 获取当前用户的所有 AiChatMessage
     */
    @GetMapping("/chat/messages")
    @ResponseBody
    public List<AiChatMessage> getUserMessages(HttpSession session) {
        Object userIdObj = session.getAttribute("id");
        if (userIdObj == null) {
            return List.of();
        }
        String userId = userIdObj.toString();
        return aiChatMessageService.listByCreateBy(userId);
    }

    @Autowired
    private AIChatMessageService aiChatMessageService;
}
