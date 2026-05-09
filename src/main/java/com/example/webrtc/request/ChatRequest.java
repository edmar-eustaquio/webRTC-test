package com.example.webrtc.request;

public class ChatRequest {

    private String roomId;

    private String type;

    private String content;

    public ChatRequest(String roomId, String type, String content) {
        this.roomId = roomId;
        this.type = type;
        this.content = content;
    }

    public ChatRequest() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
