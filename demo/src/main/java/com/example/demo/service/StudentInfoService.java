package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.domain.dto.StudentInfo;

import javax.servlet.http.HttpSession;

public interface StudentInfoService extends IService<StudentInfo> {
	StudentInfo getByUserId(HttpSession session);
	
	/**
	 * 分页查询所有学生信息
	 * @param page 分页对象
	 * @return 分页结果
	 */
	IPage<StudentInfo> getAllStudentInfoPage(Page<StudentInfo> page);
//	boolean saveOrUpdateByUserId(Long userId, StudentInfo entity);
}
