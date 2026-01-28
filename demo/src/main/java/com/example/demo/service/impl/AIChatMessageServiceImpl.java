package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.config.SessionHolder;
import com.example.demo.domain.dto.AiChatMessage;
import com.example.demo.domain.dto.User;
import com.example.demo.mapper.AIChatMessageMapper;
import com.example.demo.service.AIChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AIChatMessageServiceImpl extends ServiceImpl<AIChatMessageMapper, AiChatMessage> implements AIChatMessageService {
	@Override
	public List<AiChatMessage> listByCreateBy(String createBy) {
		LambdaQueryWrapper<AiChatMessage> query = new LambdaQueryWrapper<>();
		query.eq(AiChatMessage::getCreateBy, createBy);
		return this.list(query);
	}

	@Override
	public List<AiChatMessage> findByDateRange(Long createBy, Long start, Long end){

		List<AiChatMessage> historyMsg = lambdaQuery()
				.eq(AiChatMessage::getCreateBy, createBy)
				.between(AiChatMessage::getCreateAt, start, end)
				.list();
//		log.info("查询用户-userId:{}, start:{}, end:{}", createBy, start, end);
//		log.info("查询到的历史消息为：{}" , historyMsg);

		return historyMsg;
	}
}
