package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.domain.dto.StudentInfo;
import com.example.demo.mapper.StudentInfoMapper;
import com.example.demo.service.StudentInfoService;
import org.springframework.stereotype.Service;

@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements StudentInfoService {

	@Override
	public StudentInfo getByUserId(Long userId){
		return lambdaQuery()
				.eq(StudentInfo::getUserId, userId)
				.eq(StudentInfo::getStatus, 0)// 去掉逻辑删除
				.one();
	}




}
