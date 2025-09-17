package com.unibuc.projectmanagementapp.services;

import com.unibuc.projectmanagementapp.domain.security.User;
import com.unibuc.projectmanagementapp.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}