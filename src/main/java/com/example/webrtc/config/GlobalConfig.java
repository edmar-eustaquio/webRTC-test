package com.example.webrtc.config;

import com.example.webrtc.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

//    @Bean
//    public CallHandler callHandler(){
//        return new CallHandler();
//    }
//    @Bean
//    public ChatHandler chatHandler(){
//        return new ChatHandler();
//    }


}
