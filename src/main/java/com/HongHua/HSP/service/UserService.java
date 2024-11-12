package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.CourseMapper;
import com.HongHua.HSP.mapper.UserCourseMapper;
import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.*;
import com.HongHua.HSP.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseMapper userCourseMapper;

//    注册接口的方法
    public ResponseEntity<ApiResponse> register(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setLastEditTime(LocalDateTime.now());
        if (userMapper.findUserByEmail(user.getEmail()) == null) {
            userMapper.registerUser(user);
            User newuser = userMapper.findUserByEmail(user.getEmail());
            return ResponseEntity.status(201).body(new ApiResponse(201, "注册成功", JwtUtil.generateToken(user.getEmail(), newuser.getUserID())));
        }
        return ResponseEntity.status(400).body(new ApiResponse(400, "用户已存在", null));

    }

//    登陆接口的方法
    public ResponseEntity<ApiResponse> login(String email, String password) {
        User user = userMapper.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return ResponseEntity.status(200).body(new ApiResponse(200, "登陆成功", JwtUtil.generateToken(user.getEmail(), user.getUserID())));
        }
        return ResponseEntity.status(401).body(new ApiResponse(401, "邮箱或密码错误", null));

    }

//    修改用户信息
    public ResponseEntity<ApiResponse> modifyUserInfo(User user, String token) {
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
            return ResponseEntity.status(404).body(new ApiResponse(404, "用户不存在", null));
        }
        if (userMapper.findUserByEmail(user.getEmail()) != null){
            return ResponseEntity.status(400).body(new ApiResponse(400, "修改的电子邮箱已占用", null));
        }

        // 使用 userID 创建新用户对象
        User newuser = new User();
        newuser.setUserID(userID);
        newuser.setEmail(user.getEmail());
        newuser.setUserDesc(user.getUserDesc());
        newuser.setUserName(user.getUserName());
        newuser.setGender(user.getGender());
        newuser.setLastEditTime(user.getLastEditTime());

        // 更新用户数据
        userMapper.modifyUserData(newuser);

        // 因为前端改变的数据无需再传，所以传递一个新的token即可。因为邮箱改变了，所以要用新邮箱签发一个新token
        return ResponseEntity.status(201).body(new ApiResponse(201, "修改成功", JwtUtil.generateToken(user.getEmail(), newuser.getUserID())));
    }


//    获取用户信息
    public ResponseEntity<ApiResponse> getUserInfo(String jwtString) {
        Map<String, Object> claims = JwtUtil.validateToken(jwtString);
        String email = "";
        if (claims != null) {
            email = (String) claims.get("userEmail");
            UserDTO newuser = userMapper.findUserByEmailUseDTO(email);
            if (newuser == null){
                return ResponseEntity.status(404).body(new ApiResponse(404, "请求失败", null));
            }else{
                return ResponseEntity.status(200).body(new ApiResponse(200, "请求成功", newuser));
            }
        }else {
            return ResponseEntity.status(404).body(new ApiResponse(404, "用户不存在", null));

        }
    }

//    创建课程
    public ResponseEntity<ApiResponse> createCourse(Course course, String jwt){
        course.setCreateTime(LocalDateTime.now());
        course.setLastEditTime(LocalDateTime.now());

        Map<String, Object> claims = JwtUtil.validateToken(jwt);
        Integer userid = 0;
        if (claims != null){
            userid = (Integer) claims.get("id");

            // 创建新的userCourse表数据
            UserCourse userCourse = new UserCourse();
            userCourse.setUserID(Long.valueOf(userid));
            userCourse.setCreateTime(LocalDateTime.now());
            userCourse.setLastEditTime(LocalDateTime.now());

            // 插入课程数据, @Insert
            courseMapper.insertCourse(course);

            // 因为在@Insert下使用了Mybatis的@Options的主键和自增，在这之前并没有，所以放在@Insert后
            userCourse.setCourseID(course.getCourseID());

            // 插入用户课程
            userCourseMapper.insertUserCourse(userCourse);

            if (courseMapper.findByCourseID(course.getCourseID()) != null && userCourseMapper.findCoursesByUserID(Long.valueOf(userid)) != null){
                return ResponseEntity.status(200).body(new ApiResponse(200, "课程创建成功", null));
            }
            return ResponseEntity.status(400).body(new ApiResponse(400, "课程创建失败", null));
        }
        else{
            return ResponseEntity.status(404).body(new ApiResponse(404, "jwt错误, 用户未找到", null));
        }

    }

//    获取用户的创建的课程
    public ResponseEntity<ApiResponse> getAllUserCourse(String jwt){
        Map<String, Object> claims = JwtUtil.validateToken(jwt);
        Integer userid = 0;
        if (claims != null){
            userid = (Integer) claims.get("id");
            List<CourseDTO> courses = userCourseMapper.findCoursesByUserID(Long.valueOf(userid));
            if (!courses.isEmpty()){
                return ResponseEntity.status(200).body(new ApiResponse(200, "请求成功", courses));
            }
            return ResponseEntity.status(404).body(new ApiResponse(404, "该用户没有创建课程", null));
        }
        else {
            return ResponseEntity.status(401).body(new ApiResponse(401, "jwt错误", null));
        }
    }


}
