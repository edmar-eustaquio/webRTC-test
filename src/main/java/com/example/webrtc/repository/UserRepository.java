package com.example.webrtc.repository;

import com.example.webrtc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByIdIn(Set<Long> userIds);

    @Query(value = """
    SELECT u.* FROM users u INNER JOIN user_statuses s ON s.user_id=u.id AND s.online = 1
""", nativeQuery = true)
    List<User> getAllActiveUsers();
}
