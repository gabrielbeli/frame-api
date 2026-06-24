package com.frame.api.auth.controller;

import com.frame.api.auth.dto.AuthResponse;
import com.frame.api.auth.dto.LoginRequest;
import com.frame.api.auth.service.AuthService;
import com.frame.api.auth.dto.CurrentUserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return authService.getCurrentUser(jwt.getSubject());
    }
}
