package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.CourseMapper;
import com.HongHua.HSP.mapper.UserCourseMapper;
import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.*;
import com.HongHua.HSP.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.*;

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
//    注册接口的方法
    public ResponseEntity<ApiResponse> register(UserValidateDTO user) {

        if (userMapper.findUserByEmail(user.getEmail()) == null) {

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreateTime(LocalDateTime.now());
            user.setLastEditTime(LocalDateTime.now());

            userMapper.registerUser(user);

            // 创建 token 并设置 Cookie
            String token = JwtUtil.generateToken(user.getEmail(),user.getUserID());

            // 设置 Cookie
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true) // 确保前端无法访问该 Cookie
                    .path("/") // 设置 Cookie 的路径
                    .secure(false)
                    .maxAge(7 * 24 * 60 * 60) // 7 天过期
                    .build();
//             cookie.toString() HttpHeaders.SET_COOKIE
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(new ApiResponse(201, "注册成功",token));
        }
        return ResponseEntity.status(400).body(new ApiResponse(400, "此账号已注册", null));
    }

//    登陆接口的方法
    public ResponseEntity<ApiResponse> login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 创建 token 并设置 Cookie
            String token = JwtUtil.generateToken(user.getEmail(),user.getUserID());

            // 设置 Cookie
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true) // 确保前端无法访问该 Cookie
                    .path("/") // 设置 Cookie 的路径
                    .secure(false)
                    .maxAge(7 * 24 * 60 * 60) // 7 天过期
                    .build();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.status(200).headers(httpHeaders).body(new ApiResponse(200, "登陆成功", token));
        }
        return ResponseEntity.status(401).body(new ApiResponse(401, "邮箱或密码错误", null));

    }

//    修改用户信息 不修改ID和电子邮件
    public ResponseEntity<ApiResponse> modifyUserInfo(UserDTO user) {

        String lastUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User lastUser = userMapper.findUserByEmail(lastUserEmail);
        if (lastUser == null) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "用户不存在", null));
        }
        user.setUserID(lastUser.getUserID());

        // 更新用户数据
        userMapper.modifyUserData(user);

        return ResponseEntity.status(201).body(new ApiResponse(201, "修改成功", null));
    }


//    获取用户信息
    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    public ResponseEntity<ApiResponse> getUserInfo() {
            String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userMapper.findUserByEmail(userEmail);
            return ResponseEntity.ok(new ApiResponse(200, "请求成功", user));
        }

//    创建课程
    public ResponseEntity<ApiResponse> createCourse(Course course){
        course.setCreateTime(LocalDateTime.now());
        course.setLastEditTime(LocalDateTime.now());

        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.findUserByEmail(userEmail);

        // 创建新的userCourse表数据
        UserCourse userCourse = new UserCourse();
        userCourse.setUserID(user.getUserID());
        userCourse.setCreateTime(LocalDateTime.now());
        userCourse.setLastEditTime(LocalDateTime.now());

        // 插入课程数据, @Insert
        courseMapper.insertCourse(course);

        // 因为在@Insert下使用了Mybatis的@Options的主键和自增，在这之前并没有，所以放在@Insert后
        userCourse.setCourseID(course.getCourseID());

        // 插入用户课程
        userCourseMapper.insertUserCourse(userCourse);

        if (courseMapper.findByCourseID(course.getCourseID()) != null && userCourseMapper.findCoursesByUserID(user.getUserID()) != null){
            return ResponseEntity.status(200).body(new ApiResponse(200, "课程创建成功", null));
        }
            return ResponseEntity.status(400).body(new ApiResponse(400, "课程创建失败", null));
    }

//    获取用户的创建的课程
    public ResponseEntity<ApiResponse> getAllUserCourse(){
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.findUserByEmail(userEmail);
        List<CourseDTO> courses =  userCourseMapper.findCoursesByUserID(user.getUserID());

        if (!courses.isEmpty()){
                return ResponseEntity.status(200).body(new ApiResponse(200, "请求成功", courses));
        }else{
            return ResponseEntity.status(404).body(new ApiResponse(404, "该用户没有创建课程", null));
        }
    }
}
