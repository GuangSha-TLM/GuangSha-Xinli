package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.domain.dto.StudentInfo;
import com.example.demo.domain.dto.User;
import com.example.demo.mapper.StudentInfoMapper;
import com.example.demo.service.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements StudentInfoService {

	@Override
	public StudentInfo getByUserId(HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null || user.getId() == null){
			log.warn("未登录");
			return null;
		}

		Long userId = user.getId();
		log.info("student_info，userId = {}",userId);

		return lambdaQuery()
				.eq(StudentInfo::getUserId, userId)
				.eq(StudentInfo::getStatus, 0)// 去掉逻辑删除
				.one();

//		System.out.println("传入的 userId 值是：" + userId);
//
//		LambdaQueryWrapper<StudentInfo> wrapper = new LambdaQueryWrapper<>();
//		wrapper.eq(StudentInfo::getUserId, userId);

//		log.info("生成的 SQL: {}", wrapper.getSqlSegment());
//		return getOne(wrapper);
	}

}
