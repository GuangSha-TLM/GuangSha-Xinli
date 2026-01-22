package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.domain.dto.AiChatMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AIChatMessageMapper extends BaseMapper<AiChatMessage> {
}
