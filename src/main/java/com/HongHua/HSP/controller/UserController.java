package com.HongHua.HSP.controller;

import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.Course;
import com.HongHua.HSP.model.UserDTO;
import com.HongHua.HSP.service.UserService;
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

}
