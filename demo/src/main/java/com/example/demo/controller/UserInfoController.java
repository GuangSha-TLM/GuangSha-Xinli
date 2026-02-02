package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.domain.dto.StudentInfo;
import com.example.demo.domain.entity.ResponseCode;
import com.example.demo.service.StudentInfoService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Slf4j
@RestController
@RequestMapping("/me")
public class UserInfoController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentInfoService studentInfoService;

	@GetMapping("/info")
	public ResponseCode<StudentInfo> getInfo(HttpSession session){
		StudentInfo studentInfo = studentInfoService.getByUserId(session);

//		log.info("登入用戶完整資訊: {}", studentInfo);
//		log.info("登入用戶 ID: {}", studentInfo.getId());
		return ResponseCode.buildResponse(studentInfo);
	}

	/**
	 * 分页查询所有学生信息
	 * @param current 当前页，默认第1页
	 * @param size 每页大小，默认10条
	 * @return 分页结果
	 */
	@GetMapping("/all-students")
	public ResponseCode<IPage<StudentInfo>> getAllStudentInfo(
			@RequestParam(defaultValue = "1") Long current,
			@RequestParam(defaultValue = "10") Long size) {
		// 创建分页对象
		Page<StudentInfo> page = new Page<>(current, size);
		// 执行分页查询
		IPage<StudentInfo> result = studentInfoService.getAllStudentInfoPage(page);
		log.info("分页查询学生信息 - 当前页: {}, 每页大小: {}, 总记录数: {}", current, size, result.getTotal());
		return ResponseCode.buildResponse(result);
	}
}
