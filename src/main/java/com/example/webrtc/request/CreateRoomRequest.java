package com.example.webrtc.request;

public class CreateRoomRequest {

    private Long otherUserId;

    public CreateRoomRequest(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public CreateRoomRequest() {
    }

    public Long getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }
}
