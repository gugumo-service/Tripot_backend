package com.junior.controller.report;

import com.junior.dto.report.CreateReportDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/api/v1/reports")
    public ResponseEntity<CommonResponse<Object>> saveReport(@AuthenticationPrincipal UserPrincipal principal,
                                                             @RequestBody CreateReportDto createReportDto) {

        reportService.save(createReportDto, principal);

        return ResponseEntity.status(StatusCode.REPORT_CREATE_SUCCESS.getHttpCode()).body(CommonResponse.success(StatusCode.REPORT_CREATE_SUCCESS, null));

    }

    @PatchMapping("/api/v1/admin/reports/{report_id}/confirm")
    public ResponseEntity<CommonResponse<Object>> confirmReport(@PathVariable("report_id") Long reportId) {
        reportService.confirmReport(reportId);

        return ResponseEntity.status(StatusCode.REPORT_CONFIRM_SUCCESS.getHttpCode()).body(CommonResponse.success(StatusCode.REPORT_CONFIRM_SUCCESS, null));
    }
}
