package com.example.webrtc.request;


public class UserRequest {

    private String username;
    private String name;
    private String password;

    public UserRequest(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public UserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
