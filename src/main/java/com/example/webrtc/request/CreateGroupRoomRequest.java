package com.example.webrtc.request;

import java.util.List;

public class CreateGroupRoomRequest {

    private List<Long> otherUserIds;

    public List<Long> getOtherUserIds() {
        return otherUserIds;
    }

    public void setOtherUserIds(List<Long> otherUserIds) {
        this.otherUserIds = otherUserIds;
    }
}