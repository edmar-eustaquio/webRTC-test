package com.example.webrtc.response;

import com.example.webrtc.entity.Chat;

import java.util.List;

public record ChatRoomResponse(
        String roomId,
        String title,
        Chat lastChat,
        List<UserResponse> users
) {
}
