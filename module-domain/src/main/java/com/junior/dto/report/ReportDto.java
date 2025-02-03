package com.junior.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junior.domain.report.ReportStatus;
import com.junior.domain.report.ReportType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDto {

    private Long id;
    private String reporterUsername;
    private ReportType reportType;
    private LocalDateTime reportedTime;
    private ReportStatus reportStatus;
    private String reportReason;
    private LocalDateTime confirmTime;


}
