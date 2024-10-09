package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public ApiResponse register(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setLastEditTime(LocalDateTime.now());
        if (userMapper.findUserByEmail(user.getEmail()) == null) {
            userMapper.registerUser(user);
            return new ApiResponse(201, "注册成功", "jwt data");
        }
        return new ApiResponse(400, "用户已存在", null);
    }

    public ApiResponse login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return new ApiResponse(200, "登陆成功", "jwtData");
        }
        return new ApiResponse(401, "邮箱或密码错误", null);
    }

    public ApiResponse modifyUserInfo(User user) {
        user.setLastEditTime(LocalDateTime.now());
        userMapper.modifyUserData(user);
        User newuser = userMapper.returnUserInfo(user.getId());
        if (newuser == null) {
            return new ApiResponse(404, "用户不存在", null);
        }
        return  new ApiResponse(201, "修改成功", null);
    }

    public ApiResponse getUserInfo(Long id) {
        User newuser = userMapper.returnUserInfo(id);
        if (newuser == null) {
            return new ApiResponse(404, "用户不存在", null);
        }
        return  new ApiResponse(200, "请求成功", newuser);


    }
}
