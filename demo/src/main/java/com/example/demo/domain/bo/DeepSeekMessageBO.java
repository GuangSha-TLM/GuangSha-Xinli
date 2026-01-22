package com.example.demo.domain.bo;

import lombok.Data;

@Data
public class DeepSeekMessageBO {
    private String role; // "user" or "assistant"
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
