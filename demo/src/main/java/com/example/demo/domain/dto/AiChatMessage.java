package com.example.demo.domain.dto;

import com.example.demo.domain.entity.BaseEntity;
import lombok.Data;

@Data
public class AiChatMessage extends BaseEntity {
    private String transactionId; // 事务ID
    private String messagesJson;  // 聊天内容，json格式，包含role和content列表
}
