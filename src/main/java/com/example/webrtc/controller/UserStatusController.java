package com.example.webrtc.controller;

import com.example.webrtc.entity.User;
import com.example.webrtc.request.UserRequest;
import com.example.webrtc.service.UserService;
import com.example.webrtc.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/status/v1")
public class UserStatusController {

    @Autowired
    private UserStatusService userStatusService;


}
