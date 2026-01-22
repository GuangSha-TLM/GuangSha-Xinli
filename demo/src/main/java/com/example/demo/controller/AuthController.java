package com.example.demo.controller;

import com.example.demo.domain.dto.User;
import com.example.demo.service.UserService;
import com.example.demo.domain.entity.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseCode<?> register(@RequestBody User user) {
        boolean ok = userService.register(user);
        if (ok) {
            return ResponseCode.buildResponse("注册成功");
        } else {
            return new ResponseCode<>(ResponseCode.RESOURCE_EXISTS.getCode(), "用户名已存在");
        }
    }

    @PostMapping("/login")
    public ResponseCode<?> login(@RequestBody User user, HttpSession session) {
        User exist = userService.login(user, session);
        if (exist != null) {
            return ResponseCode.buildResponse("登录成功");
        } else {
            return new ResponseCode<>(ResponseCode.UNAUTHORIZED_ERROR.getCode(), "用户名或密码错误");
        }
    }

    @GetMapping("/logout")
    public ResponseCode<?> logout(HttpSession session) {
        userService.logout(session);
        return ResponseCode.buildResponse("已登出");
    }

    @GetMapping("/me")
    public ResponseCode<?> me(HttpSession session) {
        User user = userService.me(session);
        if (user == null) {
            return new ResponseCode<>(ResponseCode.NOT_YET_LOGIN.getCode(), "未登录");
        }
        return ResponseCode.buildResponse(user);
    }
}
