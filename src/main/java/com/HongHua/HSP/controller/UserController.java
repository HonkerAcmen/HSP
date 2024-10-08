package com.HongHua.HSP.controller;

import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.User;
import com.HongHua.HSP.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(userService.login(email, password));
    }

    @PutMapping("/modifyUserInfo")
    public ResponseEntity<ApiResponse> modifyUserInfo(@RequestBody User user) {
        return ResponseEntity.ok(userService.modifyUserInfo(user));
    }

    @GetMapping("/getUserInfo/{id}")
    public ResponseEntity<ApiResponse> getUserInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }


}
