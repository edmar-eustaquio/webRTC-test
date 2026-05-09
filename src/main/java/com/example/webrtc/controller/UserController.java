package com.example.webrtc.controller;

import com.example.webrtc.entity.User;
import com.example.webrtc.request.UserRequest;
import com.example.webrtc.service.UserService;
import com.example.webrtc.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusService userStatusService;

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping("/online")
    public List<User> getAllActiveUsers(){
        return userService.getAllActiveUsers();
    }

    @PostMapping
    public User create(@RequestBody UserRequest userRequest){
        return userService.create(userRequest);
    }

}
