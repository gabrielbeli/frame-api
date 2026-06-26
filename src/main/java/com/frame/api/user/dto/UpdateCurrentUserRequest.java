package com.frame.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateCurrentUserRequest(

        @Size(max = 120, message = "Full name must have at most 120 characters")
        String fullName,

        @Email(message = "Email must be valid")
        @Size(max = 160, message = "Email must have at most 160 characters")
        String email
) {
}
