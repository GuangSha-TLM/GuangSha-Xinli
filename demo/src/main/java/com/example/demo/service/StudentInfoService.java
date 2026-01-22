package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.domain.dto.StudentInfo;

public interface StudentInfoService extends IService<StudentInfo> {
	StudentInfo getByUserId(Long userId);
//	boolean saveOrUpdateByUserId(Long userId, StudentInfo entity);
}
