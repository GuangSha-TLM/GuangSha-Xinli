package com.example.demo.domain.bo;


import java.util.List;
import lombok.Data;
import com.example.demo.domain.bo.DeepSeekMessageBO;

@Data
public class DeepSeekPromptBO {
    private java.util.List<DeepSeekMessageBO> messages;

    public List<DeepSeekMessageBO> getMessages() {
        return messages;
    }

    public void setMessages(List<DeepSeekMessageBO> messages) {
        this.messages = messages;
    }
}
