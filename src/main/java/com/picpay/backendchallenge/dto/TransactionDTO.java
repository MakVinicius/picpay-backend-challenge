package com.picpay.backendchallenge.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull
        @Min(value = 1, message = "There has to be at least 1 sender")
        Long sender,
        @NotNull
        @Min(value = 1, message = "There has to be at least 1 receiver")
        Long receiver,
        @NotNull
        @DecimalMin(value = "1", message = "Amount must be 1 or greater")
        BigDecimal amount
) {
}
