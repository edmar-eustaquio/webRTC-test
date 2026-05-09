package com.example.webrtc.service;

import com.example.webrtc.entity.User;
import com.example.webrtc.entity.UserStatus;
import com.example.webrtc.repository.UserRepository;
import com.example.webrtc.repository.UserStatusRepository;
import com.example.webrtc.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStatusService {

    @Autowired
    private UserStatusRepository userStatusRepository;

    public Optional<UserStatus> findByUserId(Long userId){
        return userStatusRepository.findById(userId);
    }

    public void create(Long userId){
        UserStatus userStatus = new UserStatus(userId, true, false);
        userStatusRepository.save(userStatus);
    }

    public void updateOnline(Long userId, boolean online){
        UserStatus userStatus = userStatusRepository.findById(userId)
                .orElseThrow();
        userStatus.setOnline(online);
        userStatusRepository.save(userStatus);
    }

    public void updateCallIsOngoing(Long userId, boolean onCall){
        UserStatus userStatus = userStatusRepository.findById(userId)
                .orElseThrow();
        userStatus.setCallIsOngoing(onCall);
        userStatusRepository.save(userStatus);
    }

    public void deleteById(Long userId){
        userStatusRepository.deleteById(userId);
    }

}
