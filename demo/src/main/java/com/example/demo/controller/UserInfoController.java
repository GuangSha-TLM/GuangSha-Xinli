package com.example.demo.controller;

import com.example.demo.domain.dto.StudentInfo;
import com.example.demo.domain.dto.User;
import com.example.demo.domain.entity.ResponseCode;
import com.example.demo.service.StudentInfoService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/me")
public class UserInfoController {

	@Autowired
	private UserService userService;

	@Autowired
	private StudentInfoService studentInfoService;

	@GetMapping("/info")
	public ResponseCode<StudentInfo> getInfo(HttpSession session){
		User user = userService.me(session);
		if (user == null){
			return new ResponseCode<>(ResponseCode.NOT_YET_LOGIN.getCode(), "未登录");
		}

		StudentInfo studentInfo = studentInfoService.getByUserId(user.getId());
		return ResponseCode.buildResponse(studentInfo);
	}
}
