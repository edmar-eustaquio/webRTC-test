package com.example.webrtc.service;

import com.example.webrtc.entity.User;
import com.example.webrtc.entity.UserStatus;
import com.example.webrtc.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    public Optional<UserStatus> findByUserId(Long userId){
        return userStatusRepository.findById(userId);
    }

    public void create(User user){
        UserStatus userStatus = new UserStatus(user, true, false);
        userStatusRepository.save(userStatus);
    }

    public void updateOnline(Long userId, boolean online){
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User status not been saved."));
        userStatus.setOnline(online);
        userStatusRepository.save(userStatus);
    }

    public void updateCallIsOngoing(Long userId, boolean onCall){
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow();
        userStatus.setCallIsOngoing(onCall);
        userStatusRepository.save(userStatus);
    }

    public void deleteById(Long userId){
        userStatusRepository.deleteByUserId(userId);
    }

}
