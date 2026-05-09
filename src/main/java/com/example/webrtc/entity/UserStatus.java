package com.example.webrtc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_statuses")
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean online;
    private boolean callIsOngoing;

    public UserStatus() {

    }

    public UserStatus(Long id, boolean online, boolean callIsOngoing) {
        this.id = id;
        this.online = online;
        this.callIsOngoing = callIsOngoing;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isCallIsOngoing() {
        return callIsOngoing;
    }

    public void setCallIsOngoing(boolean callIsOngoing) {
        this.callIsOngoing = callIsOngoing;
    }
}
