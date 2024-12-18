package com.junior.dto.report;

import lombok.Builder;

@Builder
public record CreateReportDto(
        Long reportContentId,
        String reportType,
        String reportReason
) {
}
