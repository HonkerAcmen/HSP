package com.HongHua.HSP.controller;

import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.Course;
import com.HongHua.HSP.model.User;
import com.HongHua.HSP.model.UserDTO;
import com.HongHua.HSP.service.UserService;
import com.HongHua.HSP.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/modifyUserInfo")
    public ResponseEntity<ApiResponse> modifyUserInfo(@RequestBody UserDTO user) {
        return userService.modifyUserInfo(user);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<ApiResponse> getUserInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/createCourse")
    public ResponseEntity<ApiResponse> createCourse (@RequestBody Course course){
        return userService.createCourse(course);
    }

    @GetMapping("/getAllUserCourse")
    public ResponseEntity<ApiResponse> getUserCourse(){
        return userService.getAllUserCourse();
    }

}
