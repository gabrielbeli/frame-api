package com.frame.api.user.service;

import com.frame.api.user.dto.CreateUserRequest;
import com.frame.api.user.dto.UserResponse;
import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.entity.UserRole;
import com.frame.api.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ResourceNotFoundException;
import com.frame.api.user.dto.UpdateCurrentUserRequest;
import com.frame.api.user.dto.UpdatePasswordRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

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

    @Transactional
    public UserResponse updateCurrentUser(UUID userId, UpdateCurrentUserRequest request) {
        FrameUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.fullName() != null) {
            updateFullName(user, request.fullName());
        }

        if (request.email() != null) {
            updateEmail(user, request.email());
        }

        FrameUser updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);
    }

    @Transactional
    public void updatePassword(UUID userId, UpdatePasswordRequest request) {
        FrameUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid current password");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        String newPasswordHash = passwordEncoder.encode(request.newPassword());

        user.setPasswordHash(newPasswordHash);

        userRepository.save(user);
    }

    private void updateFullName(FrameUser user, String fullName) {
        if (fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be blank");
        }

        user.setFullName(fullName.trim());
    }

    private void updateEmail(FrameUser user, String email) {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        String normalizedEmail = email.trim().toLowerCase();

        boolean emailAlreadyExists = userRepository.existsByEmailAndIdNot(
                normalizedEmail,
                user.getId()
        );

        if (emailAlreadyExists) {
            throw new ConflictException("Email is already in use");
        }

        user.setEmail(normalizedEmail);
    }
}
