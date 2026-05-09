package com.example.webrtc.response;

public class LoginResponse {

    private String token;
    private Long userId;
    private String userName;
    private String name;

    public LoginResponse(String token, Long userId, String userName, String name) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.name = name;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
