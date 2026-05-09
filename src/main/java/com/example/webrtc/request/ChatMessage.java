package com.example.webrtc.request;

public class ChatMessage {
    String roomId, type, content;

    public ChatMessage(String roomId, String type, String content) {
        this.roomId = roomId;
        this.type = type;
        this.content = content;
    }

    public ChatMessage() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
