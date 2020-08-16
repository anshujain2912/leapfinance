package com.example.demo.controller;

import com.example.demo.objects.LoginResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/anshu")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello World!");
    }
    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@RequestParam String userName, @RequestParam String password){
        LoginResponse response=userService.validateUser(userName,password);
        return new ResponseEntity<LoginResponse>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

}
