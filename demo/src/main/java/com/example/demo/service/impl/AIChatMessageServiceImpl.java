package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.domain.dto.AiChatMessage;
import com.example.demo.mapper.AIChatMessageMapper;
import com.example.demo.service.AIChatMessageService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;

@Service
public class AIChatMessageServiceImpl extends ServiceImpl<AIChatMessageMapper, AiChatMessage> implements AIChatMessageService {
	@Override
	public List<AiChatMessage> listByCreateBy(String createBy) {
		LambdaQueryWrapper<AiChatMessage> query = new LambdaQueryWrapper<>();
		query.eq(AiChatMessage::getCreateBy, createBy);
		return this.list(query);
	}
}
