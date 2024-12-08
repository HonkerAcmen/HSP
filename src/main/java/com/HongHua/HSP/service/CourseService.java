package com.HongHua.HSP.service;

import com.HongHua.HSP.mapper.*;
import com.HongHua.HSP.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CourseService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseMapper userCourseMapper;

    @Autowired
    private JoinedCoursesMapper joinedCoursesMapper;

    /**
     * 获取当前登录用户
     * @return 当前用户对象
     */
    private User getCurrentUser() {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.findUserByEmail(userEmail);
    }

    /**
     * 创建课程
     * @param course 课程对象
     * @return 响应结果
     */
    public ResponseEntity<ApiResponse> createCourse(Course course) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "用户未登录", null));
        }

        // 设置课程信息
        course.setCreateTime(LocalDateTime.now());
        course.setLastEditTime(LocalDateTime.now());
        courseMapper.insertCourse(course);

        // 绑定用户与课程关系
        UserCourse userCourse = new UserCourse();
        userCourse.setUserID(user.getUserID());
        userCourse.setCourseID(course.getCourseID());
        userCourse.setCreateTime(LocalDateTime.now());
        userCourse.setLastEditTime(LocalDateTime.now());
        userCourseMapper.insertUserCourse(userCourse);

        // 设置课程所有者信息
        String ownerName = (user.getUserName() != null) ? user.getUserName() : "你没有名字哦";
        courseMapper.updateCourseOwner(course.getCourseID(), ownerName + "#" + user.getEmail());

        return ResponseEntity.ok(new ApiResponse(200, "课程创建成功", null));
    }

    /**
     * 获取用户创建的所有课程
     * @return 响应结果
     */
    public ResponseEntity<ApiResponse> getAllUserCourses() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "用户未登录", null));
        }

        List<CourseDTO> courses = userCourseMapper.findCoursesDTOByUserID(user.getUserID());
        if (courses.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "该用户没有创建课程", null));
        }

        return ResponseEntity.ok(new ApiResponse(200, "请求成功", courses));
    }

    /**
     * 根据课程ID获取课程详情
     * @param courseID 课程ID
     * @return 响应结果
     */
    public ResponseEntity<ApiResponse> getUserCourse(Long courseID) {
        CourseDTO courseDTO = courseMapper.findCourseByIDDTO(courseID);
        if (courseDTO == null) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "课程不存在", null));
        }

        return ResponseEntity.ok(new ApiResponse(200, "请求成功", courseDTO));
    }

    /**
     * 获取所有课程
     * @return 响应结果
     */
    public ResponseEntity<ApiResponse> getAllCourses() {
        List<CourseDTO> courses = courseMapper.allCourseData();
        if (courses.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "没有课程", null));
        }

        return ResponseEntity.ok(new ApiResponse(200, "请求成功", courses));
    }

    /**
     * 加入课程
     * @param courseID 课程ID
     * @return 响应结果
     */
    public ResponseEntity<ApiResponse> joinCourse(Long courseID) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "用户未登录", null));
        }

        Course course = courseMapper.findByCourseID(courseID);
        if (course == null) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "课程不存在", null));
        }

        // 检查是否是用户自己创建的课程
        List<CourseDTO> userCourses = userCourseMapper.findCoursesDTOByUserID(user.getUserID());
        for (CourseDTO userCourse : userCourses) {
            if (Objects.equals(courseID, userCourse.getCourseID())) {
                return ResponseEntity.status(400).body(new ApiResponse(400, "自己无法加入自己的课程", null));
            }
        }

        // 添加到加入的课程
        JoinedCourses joinedCourses = new JoinedCourses();
        joinedCourses.setUserID(user.getUserID());
        joinedCourses.setCourseID(courseID);
        joinedCourses.setCreateTime(LocalDateTime.now());
        joinedCourses.setLastEditTime(LocalDateTime.now());
        joinedCoursesMapper.insertJoinCourse(joinedCourses);

        return ResponseEntity.ok(new ApiResponse(200, "加入成功", null));
    }

    /**
     * 获取用户加入的所有课程
     * @return 响应结果
     */
    public ResponseEntity<ApiResponse> getJoinedCourses() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "用户未登录", null));
        }

        List<CourseDTO> joinedCourses = joinedCoursesMapper.returnUserOfCourse(user.getUserID());
        if (joinedCourses.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(404, "该用户没有加入任何课程", null));
        }

        return ResponseEntity.ok(new ApiResponse(200, "获取成功", joinedCourses));
    }
}
