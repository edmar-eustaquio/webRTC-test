package com.example.webrtc.handler;

import com.example.webrtc.entity.ChatRoom;
import com.example.webrtc.entity.User;
import com.example.webrtc.request.CallMessage;
import com.example.webrtc.service.ChatRoomService;
import com.example.webrtc.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CallHandler implements WebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    // ======================================================
    // userId -> websocket session
    // ======================================================
    private final Map<Long, WebSocketSession> sessions =
            new ConcurrentHashMap<>();

    // ======================================================
    // websocket sessionId -> authenticated user
    // ======================================================
    private final Map<String, User> sessionUsers =
            new ConcurrentHashMap<>();

    // ======================================================
    // roomId -> users in room
    // ======================================================
    private final Map<String, Set<Long>> roomUsers =
            new ConcurrentHashMap<>();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ChatRoomService chatRoomService;

    // ======================================================
    // CONNECT
    // ======================================================
    @Override
    public void afterConnectionEstablished(
            @NonNull WebSocketSession session
    ) throws Exception {

        User user = jwtUtil.validateSignalAndExtractUser(session);

        if (user == null)
            return;

        sessions.put(user.getId(), session);

        sessionUsers.put(session.getId(), user);

        System.out.println(
                "CONNECTED USER: "
                        + user.getId()
                        + " "
                        + user.getUsername()
        );
    }

    // ======================================================
    // HANDLE MESSAGE
    // ======================================================
    @Override
    public void handleMessage(
            WebSocketSession session,
            WebSocketMessage<?> message
    ) throws Exception {

        User user = sessionUsers.get(session.getId());

        if (user == null) {

            if (session.isOpen()) {
                session.close(
                        CloseStatus.POLICY_VIOLATION
                                .withReason("Unauthorized")
                );
            }

            return;
        }

        String jsonStr = message.getPayload().toString();

        CallMessage msg =
                mapper.readValue(jsonStr, CallMessage.class);
        msg.setSenderId(user.getId());

        switch (msg.getType()) {

            // ==================================================
            // JOIN ROOM
            // ==================================================
            case "join":
                joinRoom(session, user, msg);
                break;

            // ==================================================
            // SIGNALING
            // ==================================================
            case "off-video":
            case "on-video":
            case "mute-audio":
            case "unmute-audio":
            case "end-call":
            case "offer":
            case "answer":
            case "ice":
                forwardSignal(session, user, msg);
                break;

            default:
                System.out.println("UNKNOWN TYPE: " + msg.getType());
        }
    }

    // ======================================================
    // JOIN ROOM
    // ======================================================
    private void joinRoom(
            WebSocketSession session,
            User user,
            CallMessage msg
    ) throws Exception {

        Optional<ChatRoom> optionalRoom =
                chatRoomService.findRoomByRoomIdAndContainsUserId(
                        msg.getRoomId(),
                        user.getId()
                );

        if (optionalRoom.isEmpty()) {

            if (session.isOpen()) {
                session.close(
                        CloseStatus.BAD_DATA
                                .withReason("Invalid room")
                );
            }

            return;
        }

        ChatRoom room = optionalRoom.get();

        Set<Long> userIds = Arrays.stream(
                        room.getUserIds().split(",")
                )
                .map(Long::valueOf)
                .collect(java.util.stream.Collectors.toSet());

        roomUsers.put(room.getRoomId(), userIds);

        System.out.println(
                "JOIN ROOM: "
                        + user.getUsername()
                        + " -> "
                        + room.getRoomId()
        );

        // notify other users
        TextMessage message = new TextMessage("""
                        {
                            "type":"user-joined",
                            "id":"%s"
                        }
                        """.formatted(user.getId()));

        for (Long id : userIds) {

            if (Objects.equals(id, user.getId())) continue;

            WebSocketSession targetSession = sessions.get(id);

            if (targetSession == null ||
                    !targetSession.isOpen()) {
                continue;
            }

            try {
                targetSession.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ======================================================
    // FORWARD SIGNAL
    // ======================================================
    private void forwardSignal(
            WebSocketSession sender,
            User user,
            CallMessage msg
    ) throws Exception {

        Set<Long> userIds = roomUsers.get(msg.getRoomId());

        if (userIds == null ||
                !userIds.contains(user.getId())) {

            if (sender.isOpen()) {
                sender.close(
                        CloseStatus.BAD_DATA
                                .withReason("Invalid room")
                );
            }

            return;
        }

        msg.setSenderId(user.getId());

        String json = mapper.writeValueAsString(msg);
        TextMessage message = new TextMessage(json);

        for (Long id : userIds) {

            if (Objects.equals(id, user.getId())) {
                continue;
            }

            WebSocketSession targetSession =
                    sessions.get(id);

            if (targetSession == null ||
                    !targetSession.isOpen()) {
                continue;
            }

            try {

                targetSession.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(
                "FORWARDED "
                        + msg.getType()
                        + " IN ROOM "
                        + msg.getRoomId()
        );
    }

    // ======================================================
    // DISCONNECT
    // ======================================================
    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status
    ) {

        User user = sessionUsers.remove(session.getId());

        if (user == null) return;

        sessions.remove(user.getId());

        // remove user from all rooms
        for (Map.Entry<String, Set<Long>> entry :
                roomUsers.entrySet()) {

            Set<Long> ids = entry.getValue();

            ids.remove(user.getId());

            // cleanup empty room
            if (ids.isEmpty()) {
                roomUsers.remove(entry.getKey());
            }
        }

        System.out.println(
                "DISCONNECTED USER: "
                        + user.getUsername()
        );
    }

    // ======================================================
    // ERROR
    // ======================================================
    @Override
    public void handleTransportError(
            WebSocketSession session,
            Throwable exception
    ) {

        System.out.println(
                "SOCKET ERROR: "
                        + exception.getMessage()
        );
    }

    // ======================================================
    // PARTIAL
    // ======================================================
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}