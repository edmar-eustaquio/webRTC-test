package com.example.webrtc.repository;

import com.example.webrtc.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByRoomId(String roomId);

    @Query(value = """
    SELECT *
    FROM (
        SELECT c.*,
               ROW_NUMBER() OVER (PARTITION BY c.room_id ORDER BY c.sent_at DESC) as rn
        FROM chats c
        WHERE c.room_id IN :roomIds
    ) ranked
    WHERE rn = 1
""", nativeQuery = true)
    List<Chat> findLatestPerRoomIds(@Param("roomIds") Set<String> roomIds);
}
