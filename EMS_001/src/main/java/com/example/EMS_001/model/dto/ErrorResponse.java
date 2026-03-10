package com.example.EMS_001.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int statusCode,
        String shortSummaryMessage,
        List<String> errorList,
        LocalDateTime timestamp
) {
}
