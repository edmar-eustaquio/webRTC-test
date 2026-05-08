package com.example.webrtc.config;

public class SignalMessage {
    private String type;       // "join", "offer", "answer", "ice"

    private String roomId;     // which room (test-room, etc.)

    private String senderId;   // who sent this message (VERY important for multi-peer)

    private String targetId;   // optional: specific peer (used in multi-peer routing)

    private String offer;     // SDP offer (JSON string)
    private String answer;    // SDP answer (JSON string)

    private String candidate; // ICE candidate (JSON string)

    public SignalMessage() {}

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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }
}