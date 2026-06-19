package com.frame.api.user.service;

import com.frame.api.user.dto.CreateUserRequest;
import com.frame.api.user.dto.UserResponse;
import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.entity.UserRole;
import com.frame.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(CreateUserRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        FrameUser user = new FrameUser(
                request.fullName().trim(),
                normalizedEmail,
                UserRole.MEMBER
        );

        FrameUser savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}
