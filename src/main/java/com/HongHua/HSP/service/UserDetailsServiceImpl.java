package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findUserByEmail(email); // 通过邮箱查找用户
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // 这里构建 UserDetails 对象，角色设为 "USER" 默认为一类用户，没有 role 字段
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // 使用电子邮件作为用户名
                .password(user.getPassword()) // 获取到的加密密码
                .roles("USER") // 默认角色
                .build();
    }
}