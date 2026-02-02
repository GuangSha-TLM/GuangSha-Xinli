package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.config.SessionHolder;
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
	public StudentInfo getByUserId(HttpSession session) {
		User user = SessionHolder.getSession(session);
//		if (user == null || user.getId() == null){
//			log.warn("未登录");
//			return null;
//		}
		log.info("session 中的 user: {}", user);
		Long userId = user.getId();
		log.info("student_info的userId = {}", userId);

		StudentInfo one = lambdaQuery()
				.eq(StudentInfo::getUserId, userId)
				.one();

		log.info(String.valueOf(one));
		return one;

	}

	@Override
	public IPage<StudentInfo> getAllStudentInfoPage(Page<StudentInfo> page) {
		// 使用 MyBatis Plus 的 page 方法进行分页查询
		return this.page(page, Wrappers.lambdaQuery());
	}
}
