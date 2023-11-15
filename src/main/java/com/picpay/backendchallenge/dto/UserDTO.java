package com.picpay.backendchallenge.dto;

import com.picpay.backendchallenge.enums.UserType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UserDTO(
        @NotNull(message = "User type can't be null")
        UserType userType,
        @NotBlank(message = "Full name can't be null or empty")
        @Size(min = 3, max = 100, message = "Full name must have between 3 and 100 characters")
        String fullName,
        @NotBlank(message = "Email can't be null")
        @Email(message = "Email must follow an email pattern")
        String email,
        @NotBlank(message = "Password cannot be null or empty")
        @Size(min = 8, message = "Password must have at least 8 characters")
        String password,
        @NotBlank(message = "Document cannot be null")
        // @Size(min = 15, max = 19)
        @Pattern(
            regexp = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$",
            message = "Document must either be a CPF or CNPJ"
        )
        String document,
        @NotNull(message = "balance can't be null or empty")
        @DecimalMin(value = "0.00", message = "balance can't be less than zero")
        BigDecimal balance
) {
}
