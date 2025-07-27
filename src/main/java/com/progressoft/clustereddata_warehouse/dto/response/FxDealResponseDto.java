package com.progressoft.clustereddata_warehouse.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FxDealResponseDto(@NotBlank @Size String id, @NotBlank String fromCurrency,
                                @NotBlank String toCurrency, @NotNull LocalDateTime dealTimestamp,
                                @NotNull @Positive Double dealAmount


) {
}
