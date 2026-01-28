package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.domain.dto.AiChatMessage;

import java.util.List;

public interface AIChatMessageService extends IService<AiChatMessage> {
	/**
	 * 根据 createBy 查询所有 AiChatMessage
	 */
	List<AiChatMessage> listByCreateBy(String createBy);

	/**
	 * start 时间范围的起始
	 * end 时间范围的结束
	 */
	List<AiChatMessage> findByDateRange(Long createBy, Long start, Long end);
}
