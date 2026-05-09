package com.example.webrtc.repository;

import com.example.webrtc.entity.User;
import com.example.webrtc.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

}
