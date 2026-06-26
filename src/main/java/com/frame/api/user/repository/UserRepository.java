package com.frame.api.user.repository;

import com.frame.api.user.entity.FrameUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<FrameUser, UUID> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
    Optional<FrameUser> findByEmail(String email);
}
