package com.example.webrtc.service;

import com.example.webrtc.entity.User;
import com.example.webrtc.handler.ChatHandler;
import com.example.webrtc.repository.UserRepository;
import com.example.webrtc.request.ChatMessageReq;
import com.example.webrtc.request.LoginRequest;
import com.example.webrtc.request.RegisterRequest;
import com.example.webrtc.response.CustomUserDetails;
import com.example.webrtc.response.LoginResponse;
import com.example.webrtc.response.OnlineUserMessage;
import com.example.webrtc.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private ChatHandler chatHandler;

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName()
        );

        userStatusService.updateOnline(userDetails.getId(), true);

        chatHandler.broadcast(userDetails.getId(),
                new OnlineUserMessage(
                        "new-online-user",
                        userDetails.getId(),
                        userDetails.getName()
                )
        );

        return new LoginResponse(
                token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName()
        );
    }

    public void register(RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getName(),
                encodedPassword
        );
        User savedUser = userRepository.save(user);
        userStatusService.create(savedUser.getId());
    }

    public void logout(Long userId) throws Exception {
        userStatusService.updateOnline(userId, false);
        chatHandler.broadcast(userId, new OnlineUserMessage(
                "remove-online-user",
                userId
        ));
    }

}
