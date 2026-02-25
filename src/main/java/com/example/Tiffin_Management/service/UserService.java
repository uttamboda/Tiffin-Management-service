package com.example.Tiffin_Management.service;

import com.example.Tiffin_Management.entity.User;
import com.example.Tiffin_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("This phone number is already registered.");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
