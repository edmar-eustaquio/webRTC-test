package com.example.webrtc.repository;

import com.example.webrtc.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByUserIds(String userIds);

    Optional<ChatRoom> findByRoomId(String roomId);

    Optional<ChatRoom> findByRoomIdAndUserIdsContaining(String roomId, Long userId);

    List<ChatRoom> findByUserIdsContaining(Long userId);
}
