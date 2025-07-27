package com.progressoft.clustereddata_warehouse.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(int status, LocalDateTime timestamp, String error, String path, Object message) {
}
