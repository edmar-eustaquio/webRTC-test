package com.example.webrtc.response;

import com.example.webrtc.entity.User;

public class OnlineUserMessage{

    private String type;
    private Long userId;
    private String name;

    public OnlineUserMessage(String type, Long userId, String name) {
        this.type = type;
        this.userId = userId;
        this.name = name;
    }

    public OnlineUserMessage(String type, Long userId) {
        this.type = type;
        this.userId = userId;
    }

    public OnlineUserMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
