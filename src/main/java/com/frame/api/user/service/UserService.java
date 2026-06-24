package com.frame.api.user.service;

import com.frame.api.user.dto.CreateUserRequest;
import com.frame.api.user.dto.UserResponse;
import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.entity.UserRole;
import com.frame.api.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.frame.api.common.exception.ConflictException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(CreateUserRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("Email is already in use");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        FrameUser user = new FrameUser(
                request.fullName().trim(),
                normalizedEmail,
                passwordHash,
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
