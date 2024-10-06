package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public String register(User user) {
        if (userMapper.findUserByEmail(user.getEmail()) == null) {
            userMapper.registerUser(user);
            return "{\"message\":\"Registration successful\", \"id\":" + user.getId() + "}";
        }
        return "{\"message\":\"User already exists\"}";
    }

    public String login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return "{\"message\":\"Login successful\"}";
        }
        return "{\"message\":\"Invalid email or password\"}";
    }
}
