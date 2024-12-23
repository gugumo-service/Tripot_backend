package com.junior.dto.report;

import com.junior.domain.report.ReportReason;
import com.junior.domain.report.ReportStatus;
import com.junior.domain.report.ReportType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportQueryDto {

    private Long id;
    private String reporterUsername;
    private ReportType reportType;
    private LocalDateTime reportedTime;
    private ReportStatus reportStatus;
    private ReportReason reportReason;
    private LocalDateTime confirmTime;
    private String title;
    private String content;

    @QueryProjection
    public ReportQueryDto(Long id, String reporterUsername, ReportType reportType, LocalDateTime reportedTime, ReportStatus reportStatus, ReportReason reportReason, LocalDateTime confirmTime, String title, String content) {
        this.id = id;
        this.reporterUsername = reporterUsername;
        this.reportType = reportType;
        this.reportedTime = reportedTime;
        this.reportStatus = reportStatus;
        this.reportReason = reportReason;
        this.confirmTime = confirmTime;
        this.title = title;
        this.content = content;
    }
}
