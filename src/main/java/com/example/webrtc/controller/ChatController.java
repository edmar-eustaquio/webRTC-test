package com.example.webrtc.controller;

import com.example.webrtc.entity.Chat;
import com.example.webrtc.entity.ChatRoom;
import com.example.webrtc.request.ChatRequest;
import com.example.webrtc.request.CreateGroupRoomRequest;
import com.example.webrtc.request.CreateRoomRequest;
import com.example.webrtc.response.ChatRoomResponse;
import com.example.webrtc.response.CustomUserDetails;
import com.example.webrtc.service.ChatRoomService;
import com.example.webrtc.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/v1")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomService chatRoomService;

    @GetMapping("/per-user")
    public List<ChatRoomResponse> getRoomByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return chatRoomService.getUsersHavingUserId(userDetails.getId());
    }

    @GetMapping("/{roomId}")
    public List<Chat> getChatByRoomId(@PathVariable("roomId") String roomId) {
        return chatService.getByRoomId(roomId);
    }

    @PostMapping("/per-user")
    public Chat DetailscreatePerUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChatRequest chatRequest
    ) throws Exception {
        return chatService.create(userDetails.getId(), chatRequest);
    }

    @PostMapping("/new-room")
    public ChatRoom createRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateRoomRequest createRoomRequest
            ) {
        return chatRoomService.create(userDetails.getId(), createRoomRequest.getOtherUserId());
    }

    @PostMapping("/new-group-room")
    public ChatRoom createGroupRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateGroupRoomRequest createGroupRoomRequest
    ) {
        return chatRoomService.create(userDetails.getId(), createGroupRoomRequest.getOtherUserIds());
    }

}
