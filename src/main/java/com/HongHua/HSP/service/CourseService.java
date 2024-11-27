package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.CourseMapper;
import com.HongHua.HSP.mapper.UserCourseMapper;
import com.HongHua.HSP.mapper.UserMapper;
import com.HongHua.HSP.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseMapper userCourseMapper;
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

        if(user.getUserName() == null){
            user.setUserName("你没有名字哦");
        }
        courseMapper.updateCourseOwner(course.getCourseID(), user.getUserName() + "#" + user.getEmail());

        // 插入用户课程
        userCourseMapper.insertUserCourse(userCourse);

        if (courseMapper.findByCourseID(course.getCourseID()) != null && userCourseMapper.findCoursesDTOByUserID(user.getUserID()) != null){
            return ResponseEntity.status(200).body(new ApiResponse(200, "课程创建成功", null));
        }
        return ResponseEntity.status(400).body(new ApiResponse(400, "课程创建失败", null));
    }

    //    获取用户的创建的课程
    public ResponseEntity<ApiResponse> getAllUserCourse(){
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.findUserByEmail(userEmail);
        List<CourseDTO> courses =  userCourseMapper.findCoursesDTOByUserID(user.getUserID());

        if (!courses.isEmpty()){
            return ResponseEntity.status(200).body(new ApiResponse(200, "请求成功", courses));
        }else{
            return ResponseEntity.status(404).body(new ApiResponse(404, "该用户没有创建课程", null));
        }
    }

    public ResponseEntity<ApiResponse> getUserCourse(Long courseID){
        CourseDTO courseDTO = courseMapper.findCourseByIDDTO(courseID);
        if (courseDTO != null){
            return ResponseEntity.status(200).body(new ApiResponse(200, "请求成功", courseDTO));
        }else{
            return ResponseEntity.status(404).body(new ApiResponse(404, "课程不存在", null));
        }
    }


}
