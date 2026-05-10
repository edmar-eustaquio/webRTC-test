package com.example.webrtc.repository;

import com.example.webrtc.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

    Optional<UserStatus> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
