package com.example.webrtc.service;

import com.example.webrtc.entity.User;
import com.example.webrtc.repository.UserRepository;
import com.example.webrtc.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User create(UserRequest request){
        User user = new User(null, request.getUsername(),
                request.getName(),
                request.getPassword());
        return userRepository.save(user);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.getAllActiveUsers();
    }
}
