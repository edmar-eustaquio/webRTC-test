package com.example.webrtc.service;

import com.example.webrtc.entity.User;
import com.example.webrtc.repository.UserRepository;
import com.example.webrtc.response.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
