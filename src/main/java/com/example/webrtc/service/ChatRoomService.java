package com.example.webrtc.service;

import com.example.webrtc.entity.Chat;
import com.example.webrtc.entity.ChatRoom;
import com.example.webrtc.entity.User;
import com.example.webrtc.repository.ChatRepository;
import com.example.webrtc.repository.ChatRoomRepository;
import com.example.webrtc.repository.UserRepository;
import com.example.webrtc.response.ChatRoomResponse;
import com.example.webrtc.response.UserResponse;
import com.example.webrtc.util.ChatRoomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ChatRoomResponse> getUsersHavingUserId(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByUserIdsContaining(userId);

        Set<Long> userIds = new HashSet<>();
        Set<String> roomIds = new HashSet<>();
        Map<Long, List<Long>> roomOtherIds = new HashMap<>();
        for (ChatRoom room : rooms) {
            List<Long> ids = Arrays.stream(room.getUserIds().split(","))
                    .map(Long::valueOf)
                    .filter(id -> !id.equals(userId))
                    .toList();

            roomIds.add(room.getRoomId());
            userIds.addAll(ids);
            roomOtherIds.put(room.getId(), ids);
        }

        Map<Long, UserResponse> userMap = userRepository.findByIdIn(userIds)
                .stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> new UserResponse(user.getId(), user.getName())
                ));

        Map<String, Chat> chatMap = chatRepository.findLatestPerRoomIds(roomIds)
                .stream()
                .collect(Collectors.toMap(
                        Chat::getRoomId,
                        chat -> chat
                ));

        return rooms.stream().map(room -> {
            List<UserResponse> users = roomOtherIds.getOrDefault(room.getId(), List.of())
                    .stream()
                    .map(userMap::get)
                    .filter(Objects::nonNull)
                    .toList();

            return new ChatRoomResponse(
                    room.getRoomId(),
                    room.getTitle() == null ? users.get(0).name() : room.getTitle(),
                    chatMap.get(room.getRoomId()),
                    users
            );
        }).toList();
    }

    public Optional<ChatRoom> findRoomByUserIds(List<Long> userIds) {
        String formattedUserIds = ChatRoomUtil.sortAndJoinUserIds(userIds);
        return chatRoomRepository.findByUserIds(formattedUserIds);
    }

    public Optional<ChatRoom> findRoomByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    public Optional<ChatRoom> findRoomByRoomIdAndContainsUserId(String roomId, Long userId) {
        return chatRoomRepository.findByRoomIdAndUserIdsContaining(roomId, userId);
    }

    public ChatRoom create(Long userId, List<Long> userIds) {
        userIds.add(userId);
        String formattedUserIds = ChatRoomUtil.sortAndJoinUserIds(userIds);
        chatRoomRepository.findByUserIds(formattedUserIds)
                .ifPresent(s -> {
                    throw new RuntimeException("Invalid room creation");
                });
        String roomId = String.valueOf(UUID.randomUUID());
        String roomName = String.valueOf(UUID.randomUUID());
        ChatRoom chatRoom = new ChatRoom(null, roomId, roomName, formattedUserIds);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom create(Long userId, Long otherUserId) {
        String formattedUserIds = ChatRoomUtil.sortAndJoinUserIds(List.of(userId, otherUserId));
        chatRoomRepository.findByUserIds(formattedUserIds)
                .ifPresent(s -> {
                    throw new RuntimeException("Invalid room creation");
                });
        String roomId = String.valueOf(UUID.randomUUID());
        ChatRoom chatRoom = new ChatRoom(null, roomId, null, formattedUserIds);
        return chatRoomRepository.save(chatRoom);
    }

//    public ChatRoom create(List<Long> userIds) {
//        String formattedUserIds = ChatRoomUtil.sortAndJoinUserIds(userIds);
//        chatRoomRepository.findByUserIds(formattedUserIds)
//                .ifPresent(s -> {
//                    throw new RuntimeException("Invalid room creation");
//                });
//        String roomId = String.valueOf(UUID.randomUUID());
//        String roomName = userIds.size() > 2 ? String.valueOf(UUID.randomUUID()) : null;
//        ChatRoom chatRoom = new ChatRoom(null, roomId, roomName, formattedUserIds);
//        return chatRoomRepository.save(chatRoom);
//    }

}
