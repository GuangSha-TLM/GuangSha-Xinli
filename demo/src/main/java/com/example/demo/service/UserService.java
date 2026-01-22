package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.domain.dto.User;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    User findByUsername(String username);

    /**
     * 注册用户，成功返回true，失败返回false
     */
    boolean register(User user);

    /**
     * 登录，成功返回用户对象，失败返回null
     */
    User login(User user, HttpSession session);

    /**
     * 登出
     */
    void logout(HttpSession session);

    /**
     * 获取当前登录用户
     */
    User me(HttpSession session);
}
