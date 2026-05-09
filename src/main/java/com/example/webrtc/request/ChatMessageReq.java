package com.example.webrtc.request;

public class ChatMessageReq {
    String roomId, type;

    public ChatMessageReq(String roomId, String type) {
        this.roomId = roomId;
        this.type = type;
    }

    public ChatMessageReq() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
