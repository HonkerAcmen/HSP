package com.HongHua.HSP.controller;

import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.Course;
import com.HongHua.HSP.model.JoinedCourses;
import com.HongHua.HSP.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/createCourse")
    public ResponseEntity<ApiResponse> createCourse (@RequestBody Course course){
        return courseService.createCourse(course);
    }

    @GetMapping("/getAllUserCourse")
    public ResponseEntity<ApiResponse> getAllUserCourse(){
        return courseService.getAllUserCourses();
    }

    @GetMapping("/getUserCourse")
    public ResponseEntity<ApiResponse> getUserCourse(@RequestParam Long courseID){
        return courseService.getUserCourse(courseID);
    }

    @GetMapping("/getAllCourse")
    public ResponseEntity<ApiResponse> getAllCourse() {
        return  courseService.getAllCourses();
    }

    @PostMapping("/joinCourse")
    public ResponseEntity<ApiResponse> joinCourse(@RequestParam Long courseID){
        return courseService.joinCourse(courseID);
    }

    @GetMapping("/getJoinedCourses")
    public ResponseEntity<ApiResponse> getJoinedCourses (){
        return courseService.getJoinedCourses();
    }
}
