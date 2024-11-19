package com.HongHua.HSP.controller;


import com.HongHua.HSP.model.ApiResponse;
import com.HongHua.HSP.model.User;
import com.HongHua.HSP.model.UserValidateDTO;
import com.HongHua.HSP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authUserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserValidateDTO user) {
        return userService.register(user);
    }
    @GetMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }
}
