package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.domain.dto.AiChatMessage;
import com.example.demo.mapper.AIChatMessageMapper;
import com.example.demo.service.AIChatMessageService;
import org.springframework.stereotype.Service;

@Service
public class AIChatMessageServiceImpl extends ServiceImpl<AIChatMessageMapper, AiChatMessage> implements AIChatMessageService {
}
