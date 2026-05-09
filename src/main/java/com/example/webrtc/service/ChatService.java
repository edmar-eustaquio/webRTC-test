package com.example.webrtc.service;

import com.example.webrtc.entity.Chat;
import com.example.webrtc.handler.ChatHandler;
import com.example.webrtc.repository.ChatRepository;
import com.example.webrtc.request.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatHandler chatHandler;

    public List<Chat> getByRoomId(String roomId){
        return  chatRepository.findByRoomId(roomId);
    }

    public Chat create(Long userId, ChatRequest chatRequest) throws Exception {
        chatRoomService.findRoomByRoomIdAndContainsUserId(chatRequest.getRoomId(), userId)
                .orElseThrow(() -> new RuntimeException("Invalid room"));

        Chat chat = new Chat(
                null,
                chatRequest.getRoomId(),
                userId,
                chatRequest.getType(),
                chatRequest.getContent(),
                new Timestamp(new Date().getTime())
        );

        Chat savedChat = chatRepository.save(chat);

        chatHandler.broadcast(null, userId, savedChat.getRoomId(), savedChat);

        return savedChat;
    }

}
