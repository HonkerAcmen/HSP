package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.CourseMapper;
import com.HongHua.HSP.mapper.UserCourseMapper;
import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.*;
import com.HongHua.HSP.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseMapper userCourseMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 注册用户
    public ResponseEntity<ApiResponse> register(UserValidateDTO user) {
        if (userMapper.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400, "此账号已注册", null));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setLastEditTime(LocalDateTime.now());
        userMapper.registerUser(user);

        String token = createTokenAndSetCookie(user.getEmail(), user.getUserID());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(201, "注册成功", token));
    }

    // 用户登录
    public ResponseEntity<ApiResponse> login(String email, String password) {
        User user = userMapper.findUserByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = createTokenAndSetCookie(user.getEmail(), user.getUserID());
            return ResponseEntity.ok(new ApiResponse(200, "登陆成功", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(401, "邮箱或密码错误", null));
    }

    // 修改用户信息（不修改ID和电子邮件）
    public ResponseEntity<ApiResponse> modifyUserInfo(UserDTO user) {
        String lastUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User lastUser = userMapper.findUserByEmail(lastUserEmail);

        if (lastUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(404, "用户不存在", null));
        }

        user.setUserID(lastUser.getUserID());
        userMapper.modifyUserData(user);

        updateCourseOwners(user.getUserID(), user.getUserName(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(201, "修改成功", null));
    }

    // 获取用户信息
    public ResponseEntity<ApiResponse> getUserInfo() {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userMapper.findUserByEmailIsUserDTO(userEmail);
        return ResponseEntity.ok(new ApiResponse(200, "请求成功", user));
    }

    // 私有方法 - 创建 Token 并设置 Cookie
    private String createTokenAndSetCookie(String email, Long userId) {
        String token = JwtUtil.generateToken(email, userId);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .secure(false)
                .maxAge(7 * 24 * 60 * 60)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return token;
    }

    // 私有方法 - 更新课程的拥有者信息
    private void updateCourseOwners(Long userId, String userName, String email) {
        List<CourseDTO> courses = userCourseMapper.findCoursesDTOByUserID(userId);

        if (Objects.nonNull(courses)) {
            String ownerInfo = userName + "#" + email;
            courses.forEach(course -> courseMapper.updateCourseOwner(course.getCourseID(), ownerInfo));
        }
    }
}
