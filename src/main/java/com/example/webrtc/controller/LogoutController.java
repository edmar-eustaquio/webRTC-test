package com.example.webrtc.controller;

import com.example.webrtc.response.CustomUserDetails;
import com.example.webrtc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-logout")
public class LogoutController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public void logout(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws Exception {
        authService.logout(userDetails.getId());
    }

}
