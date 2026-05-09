package com.example.webrtc.handler;

import com.example.webrtc.entity.ChatRoom;
import com.example.webrtc.entity.User;
import com.example.webrtc.request.ChatMessageReq;
import com.example.webrtc.request.RequestCallReq;
import com.example.webrtc.service.ChatRoomService;
import com.example.webrtc.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatHandler implements WebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    // sessionId -> session
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // sessionId -> roomId
    private final Map<String, List<Long>> sessionRoom = new ConcurrentHashMap<>();

    private final Map<String, User> sessionUsers =
            new ConcurrentHashMap<>();

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        User user = jwtUtil.validateSignalAndExtractUser(session);
        if (user == null) return;

        sessions.put(user.getId(), session);

        sessionUsers.put(session.getId(), user);

        System.out.println("CONNECTED USER: "
                + user.getId()
                + " "
                + sessions.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
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
        ChatMessageReq msg = mapper.readValue(jsonStr, ChatMessageReq.class);

        switch (msg.getType()) {

            // =====================
            // REQUEST CALL
            // =====================
            case "new-online-user":
            case "remove-online-user":
            case "accept-call":
            case "reject-call":
            case "cancel-request-call":
            case "request-call": {
                broadcast(
                        session,
                        user.getId(),
                        msg.getRoomId(),
                        new RequestCallReq(msg.getRoomId(), msg.getType(), user.getId(), user.getName())
                );
                break;
            }
        }
    }

    public void broadcast(WebSocketSession session, Long userId, String roomId, Object data) throws Exception {
        List<Long> userIds = sessionRoom.get(roomId);
        if (userIds == null) {
            Optional<ChatRoom> chatRoom = chatRoomService
                    .findRoomByRoomIdAndContainsUserId(roomId, userId);

            if (chatRoom.isEmpty()) {
                if (session != null) {
                    session.close(
                            CloseStatus.BAD_DATA
                                    .withReason("Invalid room")
                    );
                }

                return;
            }
            userIds = Arrays.stream(
                            chatRoom.get()
                                    .getUserIds()
                                    .split(",")
                    )
                    .map(Long::valueOf)
                    .toList();
            sessionRoom.put(
                    roomId,
                    userIds
            );
        }

        String json = (data instanceof String)
                ? String.valueOf(data)
                : mapper.writeValueAsString(data);

        TextMessage message = new TextMessage(json);

        for (Long id : userIds) {
            if (Objects.equals(id, userId)) continue;

            WebSocketSession s = sessions.get(id);
            if (s == null || !s.isOpen()) continue;

            s.sendMessage(message);
        }
    }

    public void broadcast(Long userId, Object data) throws Exception {
        String json = (data instanceof String)
                ? String.valueOf(data)
                : mapper.writeValueAsString(data);

        TextMessage message = new TextMessage(json);

        for (Map.Entry<Long, WebSocketSession> entry : sessions.entrySet()) {
            System.out.println(userId+"|"+entry.getKey());
            if (userId != null && userId.equals(entry.getKey())) continue;

            entry.getValue().sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        User user = jwtUtil.validateSignalAndExtractUser(session);
        if (user == null) return;

        sessions.remove(user.getId());

        for (Map.Entry<String, List<Long>> entry: sessionRoom.entrySet()) {

            if (!entry.getValue().contains(user.getId())) continue;

            sessionRoom.remove(entry.getKey());
        }
        System.out.println("DISCONNECTED: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println(exception.getMessage());
    }
}