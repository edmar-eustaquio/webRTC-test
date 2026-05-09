package com.example.webrtc.controller;

import com.example.webrtc.request.LoginRequest;
import com.example.webrtc.request.RegisterRequest;
import com.example.webrtc.response.CustomUserDetails;
import com.example.webrtc.response.LoginResponse;
import com.example.webrtc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest){
        authService.register(registerRequest);
    }


}
