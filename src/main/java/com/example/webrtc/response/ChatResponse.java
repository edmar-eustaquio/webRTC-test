package com.example.webrtc.response;

import java.sql.Timestamp;

public record ChatResponse(
        String roomId,
        String content,
        Timestamp sentAt
) {
}
