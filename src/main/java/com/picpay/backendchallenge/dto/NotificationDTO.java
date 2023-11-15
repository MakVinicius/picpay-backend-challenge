package com.picpay.backendchallenge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NotificationDTO (
        @NotBlank(message = "Email can't be null or empty")
        @Email(message = "Email must follow the email pattern")
        String email,
        @NotBlank(message = "Message can't be null or empty")
        String message
) {
}
