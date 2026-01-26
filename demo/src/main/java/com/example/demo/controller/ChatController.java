
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.AIChatMessageService;
import com.example.demo.domain.dto.AiChatMessage;
import com.example.demo.domain.dto.User;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private AIChatMessageService aiChatMessageService;

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
        Object userObj = session.getAttribute("user");
        if (!(userObj instanceof User)) {
            return List.of();
        }
        User user = (User) userObj;
        if (user.getId() == null) {
            return List.of();
        }
        return aiChatMessageService.listByCreateBy(String.valueOf(user.getId()));
    }

    /**
     * 根据 transactionId 获取单条历史消息
     */
    @GetMapping("/chat/message")
    @ResponseBody
    public AiChatMessage getMessageByTid(String tid, HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (!(userObj instanceof User)) {
            return null;
        }
        User user = (User) userObj;
        if (user.getId() == null) {
            return null;
        }
        // 只允许查自己的消息
        List<AiChatMessage> list = aiChatMessageService.listByCreateBy(String.valueOf(user.getId()));
        if (list == null) return null;
        return list.stream().filter(m -> tid != null && tid.equals(m.getTransactionId())).findFirst().orElse(null);
    }
}
