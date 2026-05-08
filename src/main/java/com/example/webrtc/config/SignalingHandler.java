package com.example.webrtc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SignalingHandler implements WebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    // sessionId -> session
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // sessionId -> roomId
    private final Map<String, String> sessionRoom = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("CONNECTED: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        String jsonStr = message.getPayload().toString();
        SignalMessage msg = mapper.readValue(jsonStr, SignalMessage.class);
        msg.setSenderId(session.getId());

        switch (msg.getType()) {

            // =====================
            // JOIN ROOM
            // =====================
            case "join": {
                sessionRoom.put(session.getId(), msg.getRoomId());

                System.out.println("JOIN: " + session.getId() + " -> " + msg.getRoomId());

                // notify ALL others in room (not just one)
                broadcastToRoom(session, msg.getRoomId(), """
                    {"type":"user-joined","id":"%s"}
                """.formatted(session.getId()));

                break;
            }

            // =====================
            // SIGNAL (offer/answer/ice)
            // =====================
            case "offer":
            case "answer":
            case "ice": {
                forwardToRoomExceptSender(session, msg);
                break;
            }
        }
    }

    // ======================================================
    // BROADCAST (used when new user joins)
    // ======================================================
    private void broadcastToRoom(WebSocketSession sender, String roomId, String json) throws Exception {

        for (WebSocketSession s : sessions.values()) {

            if (!s.isOpen()) continue;
            if (s.getId().equals(sender.getId())) continue;

            if (roomId.equals(sessionRoom.get(s.getId()))) {
                s.sendMessage(new TextMessage(json));
            }
        }
    }

    // ======================================================
    // FORWARD SIGNAL TO ALL PEERS IN ROOM (EXCEPT SENDER)
    // ======================================================
    private void forwardToRoomExceptSender(WebSocketSession sender, SignalMessage msg) throws Exception {

        String roomId = sessionRoom.get(sender.getId());

        if (roomId == null) return;

        String json = mapper.writeValueAsString(msg);

        for (WebSocketSession s : sessions.values()) {

            if (!s.isOpen()) continue;
            if (s.getId().equals(sender.getId())) continue;

            if (roomId.equals(sessionRoom.get(s.getId()))) {
                s.sendMessage(new TextMessage(json));
            }
        }

        System.out.println("FORWARDED " + msg.getType() + " to room " + roomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        sessionRoom.remove(session.getId());

        System.out.println("DISCONNECTED: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("ERROR: " + exception.getMessage());
    }
}