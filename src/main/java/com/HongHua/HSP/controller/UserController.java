package com.HongHua.HSP.controller;

import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.User;
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
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @PutMapping("/modifyUserInfo/{jwt}")
    public ResponseEntity<ApiResponse> modifyUserInfo(@RequestBody User user, @PathVariable("jwt") String jwt) {
        if (JwtUtil.validateToken(jwt) == null){
            return ResponseEntity.status(401).body(new ApiResponse(401, "JWT 验证未通过", null));
        }
        return userService.modifyUserInfo(user, jwt);
    }

    @GetMapping("/getUserInfo/{jwt}")
    public ResponseEntity<ApiResponse> getUserInfo(@PathVariable("jwt") String jwt) {
        // 验证 JWT
        if (JwtUtil.validateToken(jwt) == null) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "JWT 验证未通过", null));
        }
        return userService.getUserInfo(jwt);
    }


}
