package com.progressoft.clustereddata_warehouse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FxDealRequestDto(
        @NotBlank(message = "Deal ID is required and cannot be blank") @Size(min = 3, max = 255, message = "Deal ID must be between 3 and 255 characters") String id,

        @NotBlank(message = "From Currency is required and cannot be blank") String fromCurrency,

        @NotBlank(message = "To Currency is required and cannot be blank") String toCurrency,

        @NotNull(message = "Deal timestamp is required") LocalDateTime dealTimestamp,

        @NotNull(message = "Deal amount is required") @Positive(message = "Deal amount must be a positive number") Double dealAmount) {
}
