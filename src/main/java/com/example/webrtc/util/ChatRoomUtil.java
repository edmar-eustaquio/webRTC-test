package com.example.webrtc.util;

import java.util.List;
import java.util.stream.Collectors;

public final class ChatRoomUtil {

    public static String sortAndJoinUserIds(List<Long> userIds){
        return userIds.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
