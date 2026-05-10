package com.example.webrtc.controller;

import com.example.webrtc.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/status/v1")
public class UserStatusController {

    @Autowired
    private UserStatusService userStatusService;


}
