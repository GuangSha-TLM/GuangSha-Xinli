package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.domain.dto.StudentInfo;

import javax.servlet.http.HttpSession;

public interface StudentInfoService extends IService<StudentInfo> {
	StudentInfo getByUserId(HttpSession session);
//	boolean saveOrUpdateByUserId(Long userId, StudentInfo entity);
}
