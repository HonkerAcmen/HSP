package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.User;
import com.HongHua.HSP.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public ApiResponse register(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setLastEditTime(LocalDateTime.now());
        if (userMapper.findUserByEmail(user.getEmail()) == null) {
            userMapper.registerUser(user);
            return new ApiResponse(201, "注册成功", JwtUtil.generateToken(user.getEmail()));
        }
        return new ApiResponse(400, "用户已存在", null);
    }

    public ApiResponse login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return new ApiResponse(200, "登陆成功", JwtUtil.generateToken(user.getEmail()));
        }
        return new ApiResponse(401, "邮箱或密码错误", null);
    }

    public ApiResponse modifyUserInfo(User user) {

        user.setLastEditTime(LocalDateTime.now());
        userMapper.modifyUserData(user);
        User newuser = userMapper.findUserByEmail(user.getEmail());

        if (newuser == null) {
            return new ApiResponse(404, "用户不存在", null);
        }
        return  new ApiResponse(201, "修改成功", null);
    }

    public ApiResponse getUserInfo(String jwtString) {
        Map<String, Object> claims = JwtUtil.validateToken(jwtString);
        String email = "";
        if (claims != null) {
            email = (String) claims.get("userEmail");
            User newuser = userMapper.findUserByEmail(email);
            return new ApiResponse(200, "请求成功", newuser);
        }else {
            return new ApiResponse(404, "用户不存在", null);
        }
    }


}
