package com.example.demo.controller;

import com.example.demo.domain.dto.StudentInfo;
import com.example.demo.domain.dto.User;
import com.example.demo.domain.entity.ResponseCode;
import com.example.demo.service.StudentInfoService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
}
