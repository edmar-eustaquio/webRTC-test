package com.example.webrtc.config;

import com.example.webrtc.handler.CallHandler;
import com.example.webrtc.handler.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private CallHandler callHandler;

    @Autowired
    private ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(callHandler, "/signal/call")
                .setAllowedOriginPatterns("*");

        registry.addHandler(chatHandler, "/signal/chat")
                .setAllowedOriginPatterns("*");
    }
}