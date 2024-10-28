package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.User;
import com.HongHua.HSP.model.UserDTO;
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
            User newuser = userMapper.findUserByEmail(user.getEmail());
            return new ApiResponse(201, "注册成功", JwtUtil.generateToken(user.getEmail(), newuser.getId()));
        }
        return new ApiResponse(400, "用户已存在", null);
    }

    public ApiResponse login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return new ApiResponse(200, "登陆成功", JwtUtil.generateToken(user.getEmail(), user.getId()));
        }
        return new ApiResponse(401, "邮箱或密码错误", null);
    }

    public ApiResponse modifyUserInfo(User user, String token) {
        user.setLastEditTime(LocalDateTime.now());

        // 获取用户 ID
        Map<String, Object> claims = JwtUtil.validateToken(token);
        Long userID = null;
        if (claims != null) {
            // Map默认类型为Inter，转换为Long
            Object idObj = claims.get("id");
            if (idObj instanceof Integer) {
                userID = ((Integer) idObj).longValue();
            } else if (idObj instanceof Long) {
                userID = (Long) idObj;
            }
        }

        if (userID == null) {
            return new ApiResponse(404, "用户不存在", null);
        }

        // 使用 userID 创建新用户对象
        User newuser = new User();
        newuser.setId(userID);
        newuser.setEmail(user.getEmail());
        newuser.setUserDesc(user.getUserDesc());
        newuser.setUserName(user.getUserName());
        newuser.setGender(user.getGender());
        newuser.setLastEditTime(user.getLastEditTime());

        // 更新用户数据
        userMapper.modifyUserData(newuser);
        return new ApiResponse(201, "修改成功", newuser);
    }



    public ApiResponse getUserInfo(String jwtString) {
        Map<String, Object> claims = JwtUtil.validateToken(jwtString);
        String email = "";
        if (claims != null) {
            email = (String) claims.get("userEmail");
            System.out.println(email);
            UserDTO newuser = userMapper.findUserByEmailUseDTO(email);
            if (newuser == null){
                return new ApiResponse(404, "请求失败", null);
            }else{
                return new ApiResponse(200, "请求成功", newuser);
            }
        }else {
            return new ApiResponse(404, "用户不存在", null);
        }
    }


}
