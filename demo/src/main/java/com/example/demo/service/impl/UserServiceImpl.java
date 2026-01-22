package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.domain.dto.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    @Override
    public boolean register(User user) {
        if (findByUsername(user.getUsername()) != null) {
            return false;
        } else {
            save(user);
            return true;
        }
    }

    @Override
    public User login(User user, HttpSession session) {
        User exist = findByUsername(user.getUsername());
        if (exist != null && exist.getPassword().equals(user.getPassword())) {
            session.setAttribute("user", exist);
            return exist;
        } else {
            return null;
        }
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public User me(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user instanceof User) {
            return (User) user;
        }
        return null;
    }
}
