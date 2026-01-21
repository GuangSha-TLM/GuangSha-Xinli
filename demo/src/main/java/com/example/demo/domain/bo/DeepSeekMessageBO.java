package com.example.demo.domain.bo;

import lombok.Data;

@Data
public class DeepSeekMessageBO {
    private String role; // "user" or "assistant"
    private String content;
}
