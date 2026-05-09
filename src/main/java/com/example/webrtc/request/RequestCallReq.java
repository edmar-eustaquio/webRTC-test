package com.example.webrtc.request;

public class RequestCallReq {
        private String roomId;
        private String type;
        private Long senderId;
        private String name;

    public RequestCallReq(String roomId, String type, Long senderId, String name) {
        this.roomId = roomId;
        this.type = type;
        this.senderId = senderId;
        this.name = name;
    }

    public RequestCallReq() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
